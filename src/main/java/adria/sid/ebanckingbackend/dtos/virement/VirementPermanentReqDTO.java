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
    private String clientId;
    @NotNull
    private String beneficierId;
    @NotNull
    private Date prememierDateExecution;
    @NotNull
    private Date dateFinExecution;
    @NotNull
    private String frequence;
}
