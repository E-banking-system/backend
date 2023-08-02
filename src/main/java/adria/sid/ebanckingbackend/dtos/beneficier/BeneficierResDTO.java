package adria.sid.ebanckingbackend.dtos.beneficier;

import adria.sid.ebanckingbackend.ennumerations.EGender;
import adria.sid.ebanckingbackend.ennumerations.EPType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BeneficierResDTO {
    private String id;
    private String numCompte;
    private String nom;
    private String prenom;
    private String email;
}
