package adria.sid.ebanckingbackend.dtos;

import adria.sid.ebanckingbackend.dtos.authentification.AuthResDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AuthResDTOTest {

    @Test
    public void testAuthResDTO() {
        // Test data
        String accessToken = "sample_access_token";
        String refreshToken = "sample_refresh_token";
        String role = "user";

        // Create an instance of AuthResDTO
        AuthResDTO authResDTO = new AuthResDTO();

        // Set the properties
        authResDTO.setAccessToken(accessToken);
        authResDTO.setRefreshToken(refreshToken);
        authResDTO.setRole(role);

        // Test getters
        assertEquals(accessToken, authResDTO.getAccessToken());
        assertEquals(refreshToken, authResDTO.getRefreshToken());
        assertEquals(role, authResDTO.getRole());

        // Test the constructor with all arguments
        AuthResDTO authResDTOWithArgs = new AuthResDTO(accessToken, refreshToken, role, "user_id");
        assertEquals(accessToken, authResDTOWithArgs.getAccessToken());
        assertEquals(refreshToken, authResDTOWithArgs.getRefreshToken());
        assertEquals(role, authResDTOWithArgs.getRole());

        // Test the builder
        AuthResDTO authResDTOFromBuilder = AuthResDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(role)
                .build();
        assertEquals(accessToken, authResDTOFromBuilder.getAccessToken());
        assertEquals(refreshToken, authResDTOFromBuilder.getRefreshToken());
        assertEquals(role, authResDTOFromBuilder.getRole());

        // Test toString() method
        assertNotNull(authResDTO.toString());
    }
}
