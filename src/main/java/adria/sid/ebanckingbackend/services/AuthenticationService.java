package adria.sid.ebanckingbackend.services;

import adria.sid.ebanckingbackend.dtos.AuthReqDTO;
import adria.sid.ebanckingbackend.dtos.AuthResDTO;
import adria.sid.ebanckingbackend.dtos.ReqRegisterBanquierDTO;
import adria.sid.ebanckingbackend.dtos.ReqRegisterClientDTO;
import adria.sid.ebanckingbackend.ennumerations.EGender;
import adria.sid.ebanckingbackend.ennumerations.EPType;
import adria.sid.ebanckingbackend.ennumerations.ERole;
import adria.sid.ebanckingbackend.entities.Personne;
import adria.sid.ebanckingbackend.security.Token;
import adria.sid.ebanckingbackend.repositories.TokenRepository;
import adria.sid.ebanckingbackend.security.JwtService;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.repositories.UserRepository;
import adria.sid.ebanckingbackend.ennumerations.TokenType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthResDTO registerBanquier(ReqRegisterBanquierDTO request) {
    var user=new UserEntity();
    user.setId(UUID.randomUUID().toString());
    user.setNom(request.getNom());
    user.setPrenom(request.getPrenom());
    user.setEmail(request.getEmail());
    user.setTel(request.getTelephone());
    user.setOperateur(request.getOperateur());
    user.setCIN(request.getCin());
    user.setAddress(request.getAdresse());

    Personne personne=new Personne();
    personne.setId(request.getBanqueId());

    user.setPersonne(personne);
    user.setGender(EGender.valueOf(request.getGender()));

    user.setPersonneType(EPType.PHYSIQUE);
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRole(ERole.valueOf(request.getRole()));

    var savedUser = repository.save(user);

    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);
    return AuthResDTO.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
        .build();
  }

  public AuthResDTO registerClient(ReqRegisterClientDTO request) {
    var user=new UserEntity();
    user.setId(UUID.randomUUID().toString());

    if(request.getEpType().toString().equals(EPType.PHYSIQUE.toString())){
      user.setNom(request.getNom());
      user.setPrenom(request.getPrenom());
    }
    else{
      user.setRaisonSociale(request.getRaisonSociale());
      user.setRegisterNumber(request.getRegisterNumber());
    }

    user.setRIB(request.getRib());
    user.setEmail(request.getEmail());
    user.setTel(request.getTelephone());
    user.setOperateur(request.getOperateur());
    user.setCIN(request.getCin());
    user.setAddress(request.getAdresse());
    user.setGender(EGender.valueOf(request.getGender()));

    user.setPersonneType(EPType.PHYSIQUE);
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRole(ERole.valueOf(request.getRole()));

    var savedUser = repository.save(user);

    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);
    return AuthResDTO.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
  }

  public AuthResDTO authenticate(AuthReqDTO request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    var user = repository.findByEmail(request.getEmail())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthResDTO.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
        .build();
  }

  void saveUserToken(UserEntity user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  void revokeAllUserTokens(UserEntity user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.repository.findByEmail(userEmail)
              .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthResDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}
