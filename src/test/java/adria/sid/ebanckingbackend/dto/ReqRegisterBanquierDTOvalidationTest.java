package adria.sid.ebanckingbackend.dto;

import adria.sid.ebanckingbackend.dtos.ReqRegisterBanquierDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jakarta.validation.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class ReqRegisterBanquierDTOvalidationTest {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    @Test
    void testValidDto() {
        ReqRegisterBanquierDTO dto = ReqRegisterBanquierDTO.builder()
                .nom("sougrati")
                .prenom("kaoutar")
                .email("kdkzee@example.com")
                .gender("FEMALE")
                .adresse("123 Main St")
                .cin("AB123456")
                .telephone("1234567890")
                .operateur("Operator")
                .banqueId("123456")
                .password("password")
                .role("ROLE_BANQUIER")
                .build();

        assertTrue(validator.validate(dto).isEmpty());
    }

    @Test
    void testInvalidDto() {
        ReqRegisterBanquierDTO dto = ReqRegisterBanquierDTO.builder()
                .nom("")
                .prenom("Achraf")
                .email("email@email.com")
                .gender("Male")
                .adresse("123 Main St")
                .cin("AB123")
                .telephone("123456")
                .operateur("")
                .banqueId("")
                .password("")
                .role("")
                .build();

        assertFalse(validator.validate(dto).isEmpty());
    }
}
