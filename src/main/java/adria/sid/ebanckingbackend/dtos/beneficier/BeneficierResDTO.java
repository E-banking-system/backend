package adria.sid.ebanckingbackend.dtos.beneficier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BeneficierResDTO {
    private String beneficier_id;
    private String numCompte;
    private String nom;
    private String prenom;
}
