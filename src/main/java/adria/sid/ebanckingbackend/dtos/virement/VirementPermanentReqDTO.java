package adria.sid.ebanckingbackend.dtos.virement;

import adria.sid.ebanckingbackend.ennumerations.EVType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class VirementPermanentReqDTO {
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
