package adria.sid.ebanckingbackend.services;

import adria.sid.ebanckingbackend.dtos.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import adria.sid.ebanckingbackend.ennumerations.ERole;
import adria.sid.ebanckingbackend.ennumerations.TokenType;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.security.accessToken.TokenUserRepository;
import adria.sid.ebanckingbackend.repositories.UserRepository;
import adria.sid.ebanckingbackend.security.JwtService;
import adria.sid.ebanckingbackend.security.accessToken.Token;
import adria.sid.ebanckingbackend.services.authentification.AuthenticationServiceImpl;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenUserRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Mock
    private UserEntity userEntity;

    @Test
    void testRegisterClientMorale() {
        ReqRegisterClientMoraleDTO request = new ReqRegisterClientMoraleDTO();
        request.setEmail("test@example.com");
        request.setTelephone("1234567890");
        request.setOperateur("Operator");
        request.setAdresse("123 Main St");
        request.setPassword("password");

        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        String jwtToken = "jwt_token";
        when(jwtService.generateToken(any(UserEntity.class))).thenReturn(jwtToken);

        String refreshToken = "refresh_token";
        when(jwtService.generateRefreshToken(any(UserEntity.class))).thenReturn(refreshToken);

        //UserEntity savedUser = authenticationService.registerClientMorale(request);

        verify(userRepository).save(any(UserEntity.class));

        verify(jwtService).generateToken(any(UserEntity.class));

        verify(jwtService).generateRefreshToken(any(UserEntity.class));

        //assertEquals(userEntity, savedUser);
    }

    @Test
    void testRegisterClientPhysique() {
        ReqRegisterClientPhysiqueDTO request = new ReqRegisterClientPhysiqueDTO();
        request.setEmail("test@example.com");
        request.setTelephone("1234567890");
        request.setOperateur("Operator");
        request.setAdresse("123 Main St");
        request.setPassword("password");
        request.setNom("kaoutar");
        request.setCin("JC616161");
        request.setPrenom("kaoutar");
        request.setGender("FEMALE");

        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        String jwtToken = "jwt_token";
        when(jwtService.generateToken(any(UserEntity.class))).thenReturn(jwtToken);

        String refreshToken = "refresh_token";
        when(jwtService.generateRefreshToken(any(UserEntity.class))).thenReturn(refreshToken);

        //UserEntity savedUser = authenticationService.registerClientPhysique(request);

        verify(userRepository).save(any(UserEntity.class));

        verify(jwtService).generateToken(any(UserEntity.class));

        verify(jwtService).generateRefreshToken(any(UserEntity.class));

        //assertEquals(userEntity, savedUser);
    }

    @Test
    void testAuthenticate() {
        AuthReqDTO request = AuthReqDTO.builder()
                .email("john.doe@example.com")
                .password("password")
                .build();

        UserEntity user = new UserEntity();
        user.setPassword("123");
        user.setEmail("user@gmail.com");
        user.setRole(ERole.CLIENT);

        String jwtToken = "jwt_token";
        String refreshToken = "refresh_token";

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(UserEntity.class))).thenReturn(jwtToken);
        when(jwtService.generateRefreshToken(any(UserEntity.class))).thenReturn(refreshToken);

        authenticationService.authenticate(request);

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        verify(tokenRepository).save(any(Token.class));
    }

    @Test
    void testSaveUserToken() {
        UserEntity user = new UserEntity();
        user.setPassword("123");
        user.setEmail("user@gmail.com");

        String jwtToken = "jwt_token";

        Token expectedToken = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        authenticationService.saveUserToken(user, jwtToken);

        verify(tokenRepository).save(eq(expectedToken));
    }

    @Test
    void testRevokeAllUserTokens_WithValidTokens() {
        UserEntity user = new UserEntity();
        user.setPassword("123");
        user.setEmail("user@gmail.com");

        List<Token> validUserTokens = new ArrayList<>();
        Token token1 = new Token();
        validUserTokens.add(token1);

        Token token2 = new Token();
        validUserTokens.add(token2);

        when(tokenRepository.findAllValidTokenByUser(user.getId())).thenReturn(validUserTokens);

        authenticationService.revokeAllUserTokens(user);

        assertTrue(token1.isExpired());
        assertTrue(token1.isRevoked());
        assertTrue(token2.isExpired());
        assertTrue(token2.isRevoked());
        verify(tokenRepository).saveAll(validUserTokens);
    }

    @Test
    void testRevokeAllUserTokens_WithNoValidTokens() {
        UserEntity user = new UserEntity();
        user.setPassword("123");
        user.setEmail("user@gmail.com");


        List<Token> emptyList = new ArrayList<>();
        when(tokenRepository.findAllValidTokenByUser(user.getId())).thenReturn(emptyList);

        authenticationService.revokeAllUserTokens(user);

        verify(tokenRepository, never()).saveAll(anyList());
    }

}
