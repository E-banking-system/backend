package adria.sid.ebanckingbackend.services.authentification;

import adria.sid.ebanckingbackend.dtos.authentification.AuthReqDTO;
import adria.sid.ebanckingbackend.dtos.authentification.AuthResDTO;
import adria.sid.ebanckingbackend.dtos.authentification.ChangeOperateurReqDTO;
import adria.sid.ebanckingbackend.dtos.authentification.UserInfosResDTO;
import adria.sid.ebanckingbackend.dtos.client.ClientMoraleDTO;
import adria.sid.ebanckingbackend.dtos.client.ClientPhysiqueDTO;
import adria.sid.ebanckingbackend.dtos.client.ClientResDTO;
import adria.sid.ebanckingbackend.dtos.compte.CompteResDTO;
import adria.sid.ebanckingbackend.ennumerations.ERole;
import adria.sid.ebanckingbackend.entities.Compte;
import adria.sid.ebanckingbackend.exceptions.IdUserIsNotValideException;
import adria.sid.ebanckingbackend.exceptions.UserAlreadyExists;
import adria.sid.ebanckingbackend.exceptions.UserHasNotAnyCompte;
import adria.sid.ebanckingbackend.exceptions.UserNotEnabledException;
import adria.sid.ebanckingbackend.mappers.ClientMapper;
import adria.sid.ebanckingbackend.mappers.ClientMoraleMapper;
import adria.sid.ebanckingbackend.mappers.ClientPhysiqueMapper;
import adria.sid.ebanckingbackend.mappers.UserMapper;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@Slf4j
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
  private final ClientMapper clientMapper;
  private final UserMapper userMapper;

  @Override
  public Page<ClientResDTO> searchClients(Pageable pageable, String keyword) {
    Page<UserEntity> clientPage = userRepository.searchClients(ERole.CLIENT, pageable, keyword);
    log.info("Search for clients by keyword");
    return clientPage.map(clientMapper::fromUserToClientResDTO);
  }


  @Override
  public Boolean changeOperateur(ChangeOperateurReqDTO changeOperateurReqDTO) {
    Optional<UserEntity> optionalUser = userRepository.findById(changeOperateurReqDTO.getUserId());
    UserEntity user = optionalUser.orElseThrow(() -> new IdUserIsNotValideException("User ID is not valid"));
    user.setOperateur(changeOperateurReqDTO.getOperateur());
    userRepository.save(user);
    log.info("Operator field for client changed with success");
    return true;
  }

  @Override
  public UserInfosResDTO getUserInfos(String userId){
      UserEntity user=userRepository.findById(userId).orElse(null);
      if(user != null) {
        log.info("Get user infos is done");
        return userMapper.fromUserToUserResDTO(user);
      } else{
        log.warn("User id is not valide");
        throw new IdUserIsNotValideException("User id is not valide");
      }
  }

  @Override
  public Page<ClientResDTO> getClientVirements(Pageable pageable) {
    Page<UserEntity> clientsPage = userRepository.findAllUsersByRole(ERole.CLIENT, pageable);
    log.info("Get client transfers is done");
    return clientsPage.map(clientMapper::fromUserToClientResDTO);
  }

  @Override
  public UserEntity registerClientPhysique(ClientPhysiqueDTO clientPhysiqueDTO, String url) {
    String email = clientPhysiqueDTO.getEmail();
    Optional<UserEntity> existingUser = userRepository.findByEmail(email);
    if (existingUser.isPresent()) {
      log.warn("Email already exists: "+email);
      throw new UserAlreadyExists("Email already exists: " + email);
    }

    UserEntity user = clientPhysiqueMapper.fromClientPhysiqueToUser(clientPhysiqueDTO);
    String encodedPassword = passwordEncoder.encode(clientPhysiqueDTO.getPassword());
    user.setPassword(encodedPassword); // Make sure the password is properly encoded before saving
    log.info("Registered new client physique with email: {}", email);
    return getUserEntity(url, user);
  }

  @Override
  public UserEntity registerClientMorale(ClientMoraleDTO clientMoraleDTO, String url) {
    String email = clientMoraleDTO.getEmail();
    Optional<UserEntity> existingUser = userRepository.findByEmail(email);
    if (existingUser.isPresent()) {
      log.warn("Email already exists: "+ email);
      throw new UserAlreadyExists("Email already exists: " + email);
    }

    UserEntity user = clientMoraleMapper.fromClientMoraleToUser(clientMoraleDTO);
    String encodedPassword = passwordEncoder.encode(clientMoraleDTO.getPassword());
    user.setPassword(encodedPassword); // Make sure the password is properly encoded before saving
    log.info("Registered new client morale with email: {}", email);
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
        log.warn("User not verified. Please check your email for verification instructions.");
        throw new UserNotEnabledException("User not verified. Please check your email for verification instructions.");
      }

      if (user.getRole().equals(ERole.CLIENT) && user.getComptes().size() == 0) {
        log.warn("You must visit your bank to create a banking account");
        throw new UserHasNotAnyCompte("You must visit your bank to create a banking account");
      }

      var jwtToken = jwtService.generateToken(user);
      var refreshToken = jwtService.generateRefreshToken(user);
      revokeAllUserTokens(user);
      saveUserToken(user, jwtToken);
      log.info("User authenticated successfully: {}", request.getEmail());
      return AuthResDTO.builder()
              .accessToken(jwtToken)
              .refreshToken(refreshToken)
              .role(user.getRole().toString())
              .user_id(user.getId())
              .build();
    } catch (BadCredentialsException e) {
      log.warn("Invalid email or password for authentication request: {}", request.getEmail());
      throw new BadCredentialsException("Invalid email or password");
    } catch (UserHasNotAnyCompte e) {
      throw e;
    }
  }

  @Override
  public void saveUserVerificationToken(UserEntity theUser, String token) {
    var verificationToken = new VerificationToken(token, theUser);
    log.info("Save user verification token is done");
    tokenVerificationRepository.save(verificationToken);
  }

  @Override
  public String validateToken(String theToken) {
    VerificationToken token = tokenVerificationRepository.findByToken(theToken);
    if (token == null) {
      log.warn("Token invalid");
      return "Token invalid";
    }
    UserEntity user = token.getUser();
    Calendar calendar = Calendar.getInstance();
    if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
      tokenVerificationRepository.delete(token);
      log.warn("Token has expired");
      return "Token has expired";
    }
    user.setEnabled(true);
    userRepository.save(user);
    log.info("Token is valide");
    return "valid";
  }

  private UserEntity getUserEntity(String url, UserEntity user) {
    var savedUser = userRepository.save(user);

    var jwtToken = jwtService.generateToken(user);
    saveUserToken(savedUser, jwtToken);

    String verificationToken = UUID.randomUUID().toString();
    saveUserVerificationToken(user, verificationToken);

    emailSender.sendVerificationUrlByEmail(user, verificationToken, url);
    log.info("Get user entity is done");
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
    log.info("Save user token");
    tokenUserRepository.save(token);
  }

  @Override
  public void revokeAllUserTokens(UserEntity user) {
    var validUserTokens = tokenUserRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty()) {
      log.warn("Valid user tokens is empty");
      return;
    }
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    log.info("Revoke all user tokens");
    tokenUserRepository.saveAll(validUserTokens);
  }

  @Override
  public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      log.warn("Auth header is null or auth header is not started with bearer");
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
        log.info("Refreshed tokens for user with email: {}", user.getEmail());
      }
    }
  }

  @Override
  public void createPasswordResetTokenForUser(UserEntity user, String passwordToken) {
    log.info("Create password reset token for user");
    passwordResetTokenService.createPasswordResetTokenForUser(user, passwordToken);
  }

  @Override
  public String validatePasswordResetToken(String passwordResetToken) {
    log.info("Validate password reset token");
    return passwordResetTokenService.validatePasswordResetToken(passwordResetToken);
  }

  @Override
  public UserEntity findUserByPasswordToken(String passwordResetToken) {
    log.info("Find user by password token");
    return passwordResetTokenService.findUserByPasswordToken(passwordResetToken).get();
  }

  @Override
  public void resetUserPassword(UserEntity user, String newPassword) {
    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);
    log.info("Reset password for user with email: {}", user.getEmail());
  }

  @Override
  public Optional<UserEntity> findByEmail(String email) {
    log.info("Find user by email");
    return userRepository.findByEmail(email);
  }

  @Override
  public void sendPasswordResetEmail(UserEntity user, String resetPasswordUrl) {
    String verificationToken = UUID.randomUUID().toString();
    saveUserVerificationToken(user, verificationToken);

    emailSender.sentPasswordResetVerificationEmail(user,resetPasswordUrl);
    log.info("Send password reset by email");
  }
}
