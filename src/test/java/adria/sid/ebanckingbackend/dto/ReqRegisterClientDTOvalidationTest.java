package adria.sid.ebanckingbackend.dto;

import adria.sid.ebanckingbackend.dtos.ReqRegisterClientDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jakarta.validation.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class ReqRegisterClientDTOvalidationTest {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    @Test
    void testValidDto() {
        ReqRegisterClientDTO dto = ReqRegisterClientDTO.builder()
                .nom("taffah")
                .prenom("achraf")
                .rib("123456789")
                .email("test@example.com")
                .gender("MALE")
                .adresse("123 Main St")
                .cin("AB123456")
                .telephone("1234567890")
                .operateur("Operator")
                .epType("Type")
                .raisonSociale("Company")
                .registerNumber("123456")
                .password("password")
                .role("ROLE_CLIENT")
                .build();

        assertTrue(validator.validate(dto).isEmpty());
    }

    @Test
    void testInvalidDto() {
        ReqRegisterClientDTO dto = ReqRegisterClientDTO.builder()
                .nom("")
                .prenom("kaoutar")
                .rib("")
                .email("kaoutar@gmail.com")
                .gender("")
                .adresse("")
                .cin("AB12")
                .telephone("123456")
                .operateur("")
                .epType("")
                .raisonSociale("")
                .registerNumber("")
                .password("")
                .role("")
                .build();

        assertFalse(validator.validate(dto).isEmpty());
    }
}

