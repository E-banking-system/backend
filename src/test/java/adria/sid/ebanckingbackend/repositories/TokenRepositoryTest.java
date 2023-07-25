package adria.sid.ebanckingbackend.repositories;

import adria.sid.ebanckingbackend.security.accessToken.Token;
import adria.sid.ebanckingbackend.security.accessToken.TokenUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TokenRepositoryTest {

    @Mock
    private TokenUserRepository tokenRepository;

    @Test
    void testFindAllValidTokenByUser() {
        String userId = "user123";
        List<Token> expectedTokens = new ArrayList<>();
        Token token1 = new Token();
        Token token2 = new Token();
        expectedTokens.add(token1);
        expectedTokens.add(token2);

        when(tokenRepository.findAllValidTokenByUser(userId)).thenReturn(expectedTokens);

        List<Token> actualTokens = tokenRepository.findAllValidTokenByUser(userId);

        assertEquals(expectedTokens.size(), actualTokens.size());

    }

    @Test
    void testFindByToken() {
        String tokenValue = "token123";
        Token expectedToken = new Token();

        when(tokenRepository.findByToken(tokenValue)).thenReturn(Optional.of(expectedToken));

        Optional<Token> actualToken = tokenRepository.findByToken(tokenValue);

        assertEquals(expectedToken, actualToken.orElse(null));
    }
}
