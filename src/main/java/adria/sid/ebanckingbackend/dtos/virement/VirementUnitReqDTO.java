package adria.sid.ebanckingbackend.dtos.virement;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class VirementUnitReqDTO {
    @NotNull
    private Double montant;
    @NotNull
    private String numCompteClient;
    @NotNull
    private String numCompteBeneficier;
}
