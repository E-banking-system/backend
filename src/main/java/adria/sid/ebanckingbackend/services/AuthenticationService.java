package adria.sid.ebanckingbackend.services;

import adria.sid.ebanckingbackend.dtos.*;
import adria.sid.ebanckingbackend.ennumerations.EGender;
import adria.sid.ebanckingbackend.ennumerations.EPType;
import adria.sid.ebanckingbackend.ennumerations.ERole;
import adria.sid.ebanckingbackend.entities.Personne;
import adria.sid.ebanckingbackend.security.accessToken.Token;
import adria.sid.ebanckingbackend.security.accessToken.TokenUserRepository;
import adria.sid.ebanckingbackend.security.JwtService;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.repositories.UserRepository;
import adria.sid.ebanckingbackend.ennumerations.TokenType;
import adria.sid.ebanckingbackend.security.emailToken.VerificationTokenRepository;
import adria.sid.ebanckingbackend.security.emailToken.VerificationToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository userRepository;
  private final TokenUserRepository tokenUserRepository;
  private final VerificationTokenRepository tokenVerificationRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final EmailSender emailSender;

  private String generateAccessToken() {
    // Generate and return an access token using a suitable mechanism
    // For example, you can use UUID.randomUUID().toString() to generate a random token
    return UUID.randomUUID().toString();
  }

  public void saveUserVerificationToken(UserEntity theUser, String token) {
    var verificationToken = new VerificationToken(token, theUser);
    tokenVerificationRepository.save(verificationToken);
  }


  public String validateToken(String theToken) {
    VerificationToken token = tokenVerificationRepository.findByToken(theToken);
    if(token == null){
      return "Token invalid";
    }
    UserEntity user = token.getUser();
    Calendar calendar = Calendar.getInstance();
    if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
      tokenVerificationRepository.delete(token);
      return "Token a expiré";
    }
    user.setEnabled(true);
    userRepository.save(user);
    return "valid";
  }

  public UserEntity registerBanquier(ReqRegisterBanquierDTO request) {
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

    user.setRelatedPersonne(personne);
    user.setGender(EGender.valueOf(request.getGender()));

    user.setPersonneType(EPType.PHYSIQUE);
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRole(ERole.valueOf(request.getRole()));

    var savedUser = userRepository.save(user);

    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);
    return savedUser;
  }

  public UserEntity registerClientPhysique(ReqRegisterClientPhysiqueDTO request,String url) {
    var user=new UserEntity();
    user.setId(UUID.randomUUID().toString());

    user.setNom(request.getNom());
    user.setPrenom(request.getPrenom());
    user.setEmail(request.getEmail());
    user.setTel(request.getTelephone());
    user.setOperateur(request.getOperateur());
    user.setCIN(request.getCin());
    user.setAddress(request.getAdresse());
    user.setGender(EGender.valueOf(request.getGender()));

    user.setPersonneType(EPType.PHYSIQUE);
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRole(ERole.CLIENT);

    var savedUser = userRepository.save(user);

    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);

    String verificationToken = UUID.randomUUID().toString();
    saveUserVerificationToken(user, verificationToken);

    emailSender.sendVerificationUrlByEmail(user,verificationToken,url);
    return savedUser;
  }

  public UserEntity registerClientMorale(ReqRegisterClientMoraleDTO request,String url) {
    var user=new UserEntity();
    user.setId(UUID.randomUUID().toString());

    user.setEmail(request.getEmail());
    user.setTel(request.getTelephone());
    user.setOperateur(request.getOperateur());
    user.setAddress(request.getAdresse());

    user.setPersonneType(EPType.MORALE);
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRole(ERole.CLIENT);

    var savedUser = userRepository.save(user);

    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);

    String verificationToken = UUID.randomUUID().toString();
    saveUserVerificationToken(user, verificationToken);

    emailSender.sendVerificationUrlByEmail(user,verificationToken,url);
    return savedUser;
  }

  public AuthResDTO authenticate(AuthReqDTO request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    var user = userRepository.findByEmail(request.getEmail())
        .orElseThrow();
    if(user.getEnabled()) {
      var jwtToken = jwtService.generateToken(user);
      var refreshToken = jwtService.generateRefreshToken(user);
      revokeAllUserTokens(user);
      saveUserToken(user, jwtToken);
      return AuthResDTO.builder()
              .accessToken(jwtToken)
              .refreshToken(refreshToken)
              .compteActive(user.getEnabled())
              .role(user.getRole().toString())
              .build();
    }
    return null;
  }

  void saveUserToken(UserEntity user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenUserRepository.save(token);
  }

  void revokeAllUserTokens(UserEntity user) {
    var validUserTokens = tokenUserRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenUserRepository.saveAll(validUserTokens);
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
      var user = this.userRepository.findByEmail(userEmail)
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
