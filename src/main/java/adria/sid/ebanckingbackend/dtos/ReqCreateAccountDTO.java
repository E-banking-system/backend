package adria.sid.ebanckingbackend.dtos;

import adria.sid.ebanckingbackend.ennumerations.EtatCompte;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReqCreateAccountDTO {

    @NotBlank(message = "nature is required")
    private String nature;

    @NotBlank(message = "solde is required")
    private Double solde;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
}
