package adria.sid.ebanckingbackend.dtos;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReqCreateAccountDTOTest {

    @Test
    public void testReqCreateAccountDTO() {
        // Test data
        String nature = "Savings";
        Double solde = 1000.0;
        String validEmail = "test@example.com";

        // Create an instance of ReqCreateAccountDTO
        CompteDTO reqCreateAccountDTO = new CompteDTO();

        // Set the properties
        reqCreateAccountDTO.setNature(nature);
        reqCreateAccountDTO.setSolde(solde);
        reqCreateAccountDTO.setEmail(validEmail);

        // Test getters
        assertEquals(nature, reqCreateAccountDTO.getNature());
        assertEquals(solde, reqCreateAccountDTO.getSolde());
        assertEquals(validEmail, reqCreateAccountDTO.getEmail());

        // Test constructor with all arguments
        CompteDTO reqCreateAccountDTOWithArgs = new CompteDTO(nature, solde, validEmail);
        assertEquals(nature, reqCreateAccountDTOWithArgs.getNature());
        assertEquals(solde, reqCreateAccountDTOWithArgs.getSolde());
        assertEquals(validEmail, reqCreateAccountDTOWithArgs.getEmail());

        // Test no-args constructor
        CompteDTO emptyReqCreateAccountDTO = new CompteDTO();
        assertTrue(emptyReqCreateAccountDTO.getNature() == null);
        assertTrue(emptyReqCreateAccountDTO.getSolde() == null);
        assertTrue(emptyReqCreateAccountDTO.getEmail() == null);

        // Test validation with valid data
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<CompteDTO>> violations = validator.validate(reqCreateAccountDTO);
        assertTrue(violations.isEmpty());

        // Test validation with blank nature
        CompteDTO invalidNatureReqCreateAccountDTO = new CompteDTO();
        invalidNatureReqCreateAccountDTO.setSolde(solde);
        invalidNatureReqCreateAccountDTO.setEmail(validEmail);
        violations = validator.validate(invalidNatureReqCreateAccountDTO);
        assertEquals(1, violations.size());
        assertEquals("nature is required", violations.iterator().next().getMessage());

        // Test validation with invalid email
        CompteDTO invalidEmailReqCreateAccountDTO = new CompteDTO();
        invalidEmailReqCreateAccountDTO.setNature(nature);
        invalidEmailReqCreateAccountDTO.setSolde(solde);
        invalidEmailReqCreateAccountDTO.setEmail("invalid_email");
        violations = validator.validate(invalidEmailReqCreateAccountDTO);
        assertEquals(1, violations.size());
        assertEquals("Invalid email format", violations.iterator().next().getMessage());
    }
}
