package adria.sid.ebanckingbackend.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    public void setUp() {
        jwtService = new JwtService();

        // Set the values for the properties used in JwtService (e.g., secretKey, jwtExpiration, refreshExpiration)
        Key hmacSha512Key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        jwtService.secretKey = Base64.getEncoder().encodeToString(hmacSha512Key.getEncoded());
        jwtService.jwtExpiration = 3600000; // Set the expiration time in milliseconds (1 hour)
        jwtService.refreshExpiration = 604800000; // Set the refresh token expiration time in milliseconds (7 days)
    }

    @Test
    public void testGenerateToken() {
        UserDetails userDetails = User.withUsername("testuser").password("testpassword").roles("USER").build();

        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
    }

    @Test
    public void testGenerateRefreshToken() {
        UserDetails userDetails = User.withUsername("testuser").password("testpassword").roles("USER").build();

        String refreshToken = jwtService.generateRefreshToken(userDetails);

        assertNotNull(refreshToken);
    }

    @Test
    public void testIsTokenValid() {
        UserDetails userDetails = User.withUsername("testuser").password("testpassword").roles("USER").build();
        String token = jwtService.generateToken(userDetails);

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    public void testIsTokenExpired() {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("custom_claim", "custom_value");

        UserDetails userDetails = User.withUsername("testuser").password("testpassword").roles("USER").build();
        String token = jwtService.buildToken(extraClaims, userDetails, 3600000L); // Set the token to expire in 1 hour (3600000 milliseconds)

        boolean isExpired = jwtService.isTokenExpired(token);

        assertFalse(isExpired);
    }

}
