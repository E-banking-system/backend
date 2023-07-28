package adria.sid.ebanckingbackend.dtos;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthReqDTOTest {

    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidAuthReqDTO() {
        AuthReqDTO authReqDTO = AuthReqDTO.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        Set<ConstraintViolation<AuthReqDTO>> violations = validator.validate(authReqDTO);
        assertTrue(violations.isEmpty(), "No validation errors should be present for a valid AuthReqDTO");
    }

    @Test
    public void testInvalidAuthReqDTO() {
        AuthReqDTO authReqDTO = AuthReqDTO.builder()
                .email("invalid_email")
                .password("")
                .build();

        Set<ConstraintViolation<AuthReqDTO>> violations = validator.validate(authReqDTO);
        assertEquals(2, violations.size(), "There should be 2 validation errors for an invalid AuthReqDTO");

        for (ConstraintViolation<AuthReqDTO> violation : violations) {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();

            switch (propertyPath) {
                case "email":
                    assertTrue(message.contains("Invalid email format"), "Invalid email format validation error");
                    break;
                case "password":
                    assertTrue(message.contains("Password is required"), "Password is required validation error");
                    break;
                default:
                    throw new AssertionError("Unexpected property: " + propertyPath);
            }
        }
    }
}
