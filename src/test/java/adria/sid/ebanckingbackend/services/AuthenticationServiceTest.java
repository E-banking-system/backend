package adria.sid.ebanckingbackend.services;

import adria.sid.ebanckingbackend.dtos.AuthReqDTO;
import adria.sid.ebanckingbackend.dtos.AuthResDTO;
import adria.sid.ebanckingbackend.dtos.ReqRegisterBanquierDTO;
import adria.sid.ebanckingbackend.dtos.ReqRegisterClientDTO;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import adria.sid.ebanckingbackend.ennumerations.TokenType;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.repositories.TokenUserRepository;
import adria.sid.ebanckingbackend.repositories.UserRepository;
import adria.sid.ebanckingbackend.security.JwtService;
import adria.sid.ebanckingbackend.security.Token;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

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
    private AuthenticationService authenticationService;

    @Test
    void testRegisterBanquier() {
        ReqRegisterBanquierDTO request = new ReqRegisterBanquierDTO();
        request.setBanqueId("1234");
        request.setNom("sougrati");
        request.setPrenom("kaoutar");
        request.setAdresse("jhfcuiei");
        request.setEmail("kaoutar.sougrati@gmail.com");
        request.setGender("FEMALE");
        request.setRole("BANQUIER");
        request.setOperateur("orange");
        request.setPassword("123");
        request.setCin("JCHIZIESI");
        request.setTelephone("123456789");


        UserEntity savedUser = new UserEntity();
        savedUser.setPassword("123");
        savedUser.setEmail("user@gmail.com");

        String jwtToken = "jwt_token";
        String refreshToken = "refresh_token";

        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUser);
        when(jwtService.generateToken(any(UserEntity.class))).thenReturn(jwtToken);
        when(jwtService.generateRefreshToken(any(UserEntity.class))).thenReturn(refreshToken);

        AuthResDTO response = authenticationService.registerBanquier(request);

        assertEquals(jwtToken, response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
    }

    @Test
    void testRegisterClient() {
        ReqRegisterClientDTO request = new ReqRegisterClientDTO();
        request.setRegisterNumber("1234");
        request.setRib("fjikvoek");
        request.setRaisonSociale("rfiejdio");
        request.setNom("sougrati");
        request.setPrenom("kaoutar");
        request.setAdresse("jhfcuiei");
        request.setEmail("kaoutar.sougrati@gmail.com");
        request.setGender("FEMALE");
        request.setRole("BANQUIER");
        request.setOperateur("orange");
        request.setPassword("123");
        request.setCin("JCHIZIESI");
        request.setTelephone("123456789");
        request.setEpType("PHYSIQUE");


        UserEntity savedUser = new UserEntity();
        savedUser.setPassword("123");
        savedUser.setEmail("user@gmail.com");

        String jwtToken = "jwt_token";
        String refreshToken = "refresh_token";

        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUser);
        when(jwtService.generateToken(any(UserEntity.class))).thenReturn(jwtToken);
        when(jwtService.generateRefreshToken(any(UserEntity.class))).thenReturn(refreshToken);

        AuthResDTO response = authenticationService.registerClient(request);

        assertEquals(jwtToken, response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
    }

    @Test
    void testAuthenticate() {
        AuthReqDTO request = new AuthReqDTO();
        request.setEmail("test@gmail.com");
        request.setPassword("123");

        UserEntity user = new UserEntity();
        user.setPassword("123");
        user.setEmail("user@gmail.com");

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
