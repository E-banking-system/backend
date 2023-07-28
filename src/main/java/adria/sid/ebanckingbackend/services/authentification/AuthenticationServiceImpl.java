package adria.sid.ebanckingbackend.services.authentification;

import adria.sid.ebanckingbackend.dtos.*;
import adria.sid.ebanckingbackend.ennumerations.EGender;
import adria.sid.ebanckingbackend.ennumerations.EPType;
import adria.sid.ebanckingbackend.ennumerations.ERole;
import adria.sid.ebanckingbackend.exceptions.UserNotEnabledException;
import adria.sid.ebanckingbackend.security.accessToken.Token;
import adria.sid.ebanckingbackend.security.accessToken.TokenUserRepository;
import adria.sid.ebanckingbackend.security.JwtService;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.repositories.UserRepository;
import adria.sid.ebanckingbackend.ennumerations.TokenType;
import adria.sid.ebanckingbackend.security.emailToken.VerificationTokenRepository;
import adria.sid.ebanckingbackend.security.emailToken.VerificationToken;
import adria.sid.ebanckingbackend.services.email.EmailSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthentificationService {
  private final UserRepository userRepository;
  private final TokenUserRepository tokenUserRepository;
  private final VerificationTokenRepository tokenVerificationRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final EmailSender emailSender;
  private final MotdepasseTokenService passwordResetTokenService;

  @Override
  public void saveUserVerificationToken(UserEntity theUser, String token) {
    var verificationToken = new VerificationToken(token, theUser);
    tokenVerificationRepository.save(verificationToken);
  }

  @Override
  public String validateToken(String theToken) {
    VerificationToken token = tokenVerificationRepository.findByToken(theToken);
    if (token == null) {
      return "Token invalid";
    }
    UserEntity user = token.getUser();
    Calendar calendar = Calendar.getInstance();
    if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
      tokenVerificationRepository.delete(token);
      return "Token has expired";
    }
    user.setEnabled(true);
    userRepository.save(user);
    return "valid";
  }

  @Override
  public UserEntity registerClientPhysique(ReqRegisterClientPhysiqueDTO reqRegisterClientPhysiqueDTO, String url) {
    var user = new UserEntity();
    user.setId(UUID.randomUUID().toString());

    user.setNom(reqRegisterClientPhysiqueDTO.getNom());
    user.setPrenom(reqRegisterClientPhysiqueDTO.getPrenom());
    user.setEmail(reqRegisterClientPhysiqueDTO.getEmail());
    user.setTel(reqRegisterClientPhysiqueDTO.getTelephone());
    user.setOperateur(reqRegisterClientPhysiqueDTO.getOperateur());
    user.setCIN(reqRegisterClientPhysiqueDTO.getCin());
    user.setAddress(reqRegisterClientPhysiqueDTO.getAdresse());
    user.setGender(EGender.valueOf(reqRegisterClientPhysiqueDTO.getGender()));

    user.setPersonneType(EPType.PHYSIQUE);
    user.setPassword(passwordEncoder.encode(reqRegisterClientPhysiqueDTO.getPassword()));
    user.setRole(ERole.CLIENT);

    var savedUser = userRepository.save(user);

    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);

    String verificationToken = UUID.randomUUID().toString();
    saveUserVerificationToken(user, verificationToken);

    emailSender.sendVerificationUrlByEmail(user, verificationToken, url);
    return savedUser;
  }

  @Override
  public UserEntity registerClientMorale(ReqRegisterClientMoraleDTO reqRegisterClientMoraleDTO, String url) {
    var user = new UserEntity();
    user.setId(UUID.randomUUID().toString());

    user.setEmail(reqRegisterClientMoraleDTO.getEmail());
    user.setTel(reqRegisterClientMoraleDTO.getTelephone());
    user.setOperateur(reqRegisterClientMoraleDTO.getOperateur());
    user.setAddress(reqRegisterClientMoraleDTO.getAdresse());

    user.setPersonneType(EPType.MORALE);
    user.setPassword(passwordEncoder.encode(reqRegisterClientMoraleDTO.getPassword()));
    user.setRole(ERole.CLIENT);

    var savedUser = userRepository.save(user);

    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);

    String verificationToken = UUID.randomUUID().toString();
    saveUserVerificationToken(user, verificationToken);

    emailSender.sendVerificationUrlByEmail(user, verificationToken, url);
    return savedUser;
  }

  @Override
  public AuthResDTO authenticate(AuthReqDTO request) {
    try {
      authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                      request.getEmail(),
                      request.getPassword()
              )
      );
      var user = userRepository.findByEmail(request.getEmail())
              .orElseThrow(() -> new UsernameNotFoundException("User not found"));

      if (!user.getEnabled()) {
        throw new UserNotEnabledException("User not verified. Please check your email for verification instructions.");
      }

      var jwtToken = jwtService.generateToken(user);
      var refreshToken = jwtService.generateRefreshToken(user);
      revokeAllUserTokens(user);
      saveUserToken(user, jwtToken);

      return AuthResDTO.builder()
              .accessToken(jwtToken)
              .refreshToken(refreshToken)
              .role(user.getRole().toString())
              .build();
    } catch (BadCredentialsException e) {
      throw new BadCredentialsException("Invalid email or password");
    }
  }

  @Override
  public void saveUserToken(UserEntity user, String jwtToken) {
    var token = Token.builder()
            .user(user)
            .token(jwtToken)
            .tokenType(TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .build();
    tokenUserRepository.save(token);
  }

  @Override
  public void revokeAllUserTokens(UserEntity user) {
    var validUserTokens = tokenUserRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty()) {
      return;
    }
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenUserRepository.saveAll(validUserTokens);
  }

  @Override
  public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.userRepository.findByEmail(userEmail)
              .orElseThrow(() -> new UsernameNotFoundException("User not found"));
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

  @Override
  public void createPasswordResetTokenForUser(UserEntity user, String passwordToken) {
    passwordResetTokenService.createPasswordResetTokenForUser(user, passwordToken);
  }

  @Override
  public String validatePasswordResetToken(String passwordResetToken) {
    return passwordResetTokenService.validatePasswordResetToken(passwordResetToken);
  }

  @Override
  public UserEntity findUserByPasswordToken(String passwordResetToken) {
    return passwordResetTokenService.findUserByPasswordToken(passwordResetToken).get();
  }

  @Override
  public void resetUserPassword(UserEntity user, String newPassword) {
    System.out.println("The password after encoding is : "+newPassword);
    user.setPassword(passwordEncoder.encode(newPassword));
    System.out.println("The password before encoding is : "+user.getPassword());
    userRepository.save(user);
  }

  @Override
  public Optional<UserEntity> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

}
