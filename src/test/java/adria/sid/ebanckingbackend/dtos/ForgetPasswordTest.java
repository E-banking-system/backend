package adria.sid.ebanckingbackend.dtos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ForgetPasswordTest {

    @Test
    public void testForgetPassword() {
        // Test data
        String email = "test@example.com";
        String password = "new_password";

        // Create an instance of ForgetPassword
        ForgetPassword forgetPassword = new ForgetPassword();

        // Set the properties
        forgetPassword.setEmail(email);
        forgetPassword.setPassword(password);

        // Test getters
        assertEquals(email, forgetPassword.getEmail());
        assertEquals(password, forgetPassword.getPassword());

        // Test the constructor with all arguments
        ForgetPassword forgetPasswordWithArgs = new ForgetPassword(email, password);
        assertEquals(email, forgetPasswordWithArgs.getEmail());
        assertEquals(password, forgetPasswordWithArgs.getPassword());

        // Test the builder
        ForgetPassword forgetPasswordFromBuilder = ForgetPassword.builder()
                .email(email)
                .password(password)
                .build();
        assertEquals(email, forgetPasswordFromBuilder.getEmail());
        assertEquals(password, forgetPasswordFromBuilder.getPassword());

        // Test toString() method
        assertNotNull(forgetPassword.toString());
    }
}
