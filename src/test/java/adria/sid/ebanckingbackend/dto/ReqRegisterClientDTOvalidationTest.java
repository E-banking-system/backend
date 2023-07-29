package adria.sid.ebanckingbackend.dto;

import adria.sid.ebanckingbackend.dtos.client.ClientPhysiqueDTO;
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
        ClientPhysiqueDTO dto = ClientPhysiqueDTO.builder()
                .nom("taffah")
                .prenom("achraf")
                .email("test@example.com")
                .gender("MALE")
                .adresse("123 Main St")
                .cin("AB123456")
                .telephone("1234567890")
                .operateur("Operator")
                .password("password")
                .build();

        assertTrue(validator.validate(dto).isEmpty());
    }

    @Test
    void testInvalidDto() {
        ClientPhysiqueDTO dto = ClientPhysiqueDTO.builder()
                .nom("")
                .prenom("kaoutar")
                .email("kaoutar@gmail.com")
                .gender("")
                .adresse("")
                .cin("AB12")
                .telephone("123456")
                .operateur("")
                .password("")
                .build();

        assertFalse(validator.validate(dto).isEmpty());
    }
}

