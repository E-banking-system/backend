package adria.sid.ebanckingbackend.dtos.operation;

import adria.sid.ebanckingbackend.ennumerations.EVType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VirementPermaReqDTO {
    @NotNull
    private Double montant;
    @NotNull
    private String numCompteClient;
    @NotNull
    private String numCompteBeneficier;
    @NotNull
    private Date premierDateExecution;
    @NotNull
    private Date dateFinExecution;
    @NotNull
    private EVType frequence;
}
