package adria.sid.ebanckingbackend.services.authentification;

import adria.sid.ebanckingbackend.dtos.*;
import adria.sid.ebanckingbackend.ennumerations.ERole;
import adria.sid.ebanckingbackend.exceptions.UserAlreadyExists;
import adria.sid.ebanckingbackend.exceptions.UserHasNotAnyCompte;
import adria.sid.ebanckingbackend.exceptions.UserNotEnabledException;
import adria.sid.ebanckingbackend.mappers.ClientMoraleMapper;
import adria.sid.ebanckingbackend.mappers.ClientPhysiqueMapper;
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
public class AuthenticationServiceImpl implements AuthenticationService {
  private final UserRepository userRepository;
  private final TokenUserRepository tokenUserRepository;
  private final VerificationTokenRepository tokenVerificationRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final EmailSender emailSender;
  private final MotdepasseTokenServiceImpl passwordResetTokenService;
  private final ClientPhysiqueMapper clientPhysiqueMapper;
  private final ClientMoraleMapper clientMoraleMapper;


  @Override
  public UserEntity registerClientPhysique(ClientPhysiqueDTO clientPhysiqueDTO, String url) {
    String email = clientPhysiqueDTO.getEmail();
    Optional<UserEntity> existingUser = userRepository.findByEmail(email);
    if (existingUser.isPresent()) {
      throw new UserAlreadyExists("Email already exists: " + email);
    }

    UserEntity user = clientPhysiqueMapper.fromClientPhysiqueToUser(clientPhysiqueDTO);

    return getUserEntity(url, user);
  }

  @Override
  public UserEntity registerClientMorale(ClientMoraleDTO clientMoraleDTO, String url) {
    String email = clientMoraleDTO.getEmail();
    Optional<UserEntity> existingUser = userRepository.findByEmail(email);
    if (existingUser.isPresent()) {
      throw new UserAlreadyExists("Email already exists: " + email);
    }

    UserEntity user = clientMoraleMapper.fromClientMoraleToUser(clientMoraleDTO);

    return getUserEntity(url, user);
  }

  @Override
  public AuthResDTO authenticate(AuthReqDTO request) throws UserHasNotAnyCompte {
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

      if (user.getRole().equals(ERole.CLIENT) && user.getComptes().size() == 0) {
        throw new UserHasNotAnyCompte("You must visit your bank to create a banking account");
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
    } catch (UserHasNotAnyCompte e) {
      throw e;
    }
  }

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

  private UserEntity getUserEntity(String url, UserEntity user) {
    var savedUser = userRepository.save(user);

    var jwtToken = jwtService.generateToken(user);
    saveUserToken(savedUser, jwtToken);

    String verificationToken = UUID.randomUUID().toString();
    saveUserVerificationToken(user, verificationToken);

    emailSender.sendVerificationUrlByEmail(user, verificationToken, url);
    return savedUser;
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
    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);
  }

  @Override
  public Optional<UserEntity> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  public void sendPasswordResetEmail(UserEntity user, String resetPasswordUrl) {
    String verificationToken = UUID.randomUUID().toString();
    saveUserVerificationToken(user, verificationToken);

    emailSender.sentPasswordResetVerificationEmail(user,resetPasswordUrl);
  }
}
