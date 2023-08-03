package adria.sid.ebanckingbackend.dtos.beneficier;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BeneficierReqDTO {
    @NotBlank(message = "clientId is required")
    private String clientId;

    @NotBlank(message = "Nom is required")
    private String nom;

    @NotBlank(message = "Prénom is required")
    private String prenom;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Num compte is required")
    private String numCompte;
}
