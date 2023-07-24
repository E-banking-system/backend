package adria.sid.ebanckingbackend.services;

import adria.sid.ebanckingbackend.repositories.TokenRepository;
import adria.sid.ebanckingbackend.security.Token;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogoutServiceTest {

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private LogoutService logoutService;

    @Test
    void testLogout_ValidToken() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Authentication authentication = mock(Authentication.class);

        String authHeader = "Bearer valid_token";
        String jwt = "valid_token";

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(tokenRepository.findByToken(jwt)).thenReturn(Optional.of(new Token()));

        logoutService.logout(request, response, authentication);

        try {
            verify(response, never()).sendError(anyInt());
        } catch (IOException e) {
            e.printStackTrace();
        }
        verify(tokenRepository).save(any(Token.class));
        verify(authentication, never()).setAuthenticated(anyBoolean());
    }

    @Test
    void testLogout_InvalidToken() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Authentication authentication = mock(Authentication.class);

        String authHeader = "invalid_token";

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        // No need to stub tokenRepository.findByToken(jwt) as the token is invalid.

        logoutService.logout(request, response, authentication);

        try {
            verify(response, never()).sendError(anyInt());
        } catch (IOException e) {
            e.printStackTrace();
        }
        verify(tokenRepository, never()).save(any(Token.class));
        verify(authentication, never()).setAuthenticated(anyBoolean());
    }
}
