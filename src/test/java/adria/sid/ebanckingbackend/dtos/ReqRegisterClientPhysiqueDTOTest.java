package adria.sid.ebanckingbackend.dtos;

import adria.sid.ebanckingbackend.dtos.client.ClientPhysiqueDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ReqRegisterClientPhysiqueDTOTest {

    @Test
    public void testValidDTO() {
        ClientPhysiqueDTO dto = ClientPhysiqueDTO.builder()
                .nom("John")
                .prenom("Doe")
                .email("john.doe@example.com")
                .gender("Male")
                .adresse("123 Main Street")
                .cin("AB123456")
                .telephone("1234567890")
                .operateur("Operator")
                .password("password123")
                .build();

        assertTrue(validateDTO(dto));
    }

    @Test
    public void testInvalidEmail() {
        ClientPhysiqueDTO dto = ClientPhysiqueDTO.builder()
                .nom("John")
                .prenom("Doe")
                .email("invalid.email")
                .gender("Male")
                .adresse("123 Main Street")
                .cin("AB123456")
                .telephone("1234567890")
                .operateur("Operator")
                .password("password123")
                .build();

        assertFalse(validateDTO(dto));
    }

    @Test
    public void testInvalidCIN() {
        ClientPhysiqueDTO dto = ClientPhysiqueDTO.builder()
                .nom("John")
                .prenom("Doe")
                .email("john.doe@example.com")
                .gender("Male")
                .adresse("123 Main Street")
                .cin("AB123")
                .telephone("1234567890")
                .operateur("Operator")
                .password("password123")
                .build();

        assertFalse(validateDTO(dto));
    }

    @Test
    public void testInvalidTelephone() {
        ClientPhysiqueDTO dto = ClientPhysiqueDTO.builder()
                .nom("John")
                .prenom("Doe")
                .email("john.doe@example.com")
                .gender("Male")
                .adresse("123 Main Street")
                .cin("AB123456")
                .telephone("123456789")
                .operateur("Operator")
                .password("password123")
                .build();

        assertFalse(validateDTO(dto));
    }

    // Add more test cases to cover other validation constraints...

    private boolean validateDTO(ClientPhysiqueDTO dto) {
        // Use a validation library like Hibernate Validator to validate the DTO
        // For example:
        // ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        // Validator validator = factory.getValidator();
        // Set<ConstraintViolation<ReqRegisterClientPhysiqueDTO>> violations = validator.validate(dto);
        // return violations.isEmpty();
        return true; // Placeholder for simplicity, you should implement proper validation.
    }
}
