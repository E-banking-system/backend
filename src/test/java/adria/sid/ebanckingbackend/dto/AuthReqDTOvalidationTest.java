package adria.sid.ebanckingbackend.dto;


import adria.sid.ebanckingbackend.dtos.authentification.AuthReqDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jakarta.validation.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class AuthReqDTOvalidationTest {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    @Test
    void testValidDto() {
        AuthReqDTO dto = AuthReqDTO.builder()
                .email("test@gmail.com")
                .password("password")
                .build();

        assertTrue(validator.validate(dto).isEmpty());
    }

    @Test
    void testInvalidDto() {
        AuthReqDTO dto = AuthReqDTO.builder()
                .email("test@gmail.com")
                .password("")
                .build();

        assertFalse(validator.validate(dto).isEmpty());
    }
}

