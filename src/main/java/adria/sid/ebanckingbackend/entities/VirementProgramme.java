package adria.sid.ebanckingbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VirementProgramme {
    @Id
    private String id;

    private Double montant;
    private String numCompteClient;
    private String numCompteBeneficier;
    private String clientId;
    private String beneficierId;
    private Date prememierDateExecution;
    private Date dateFinExecution;
    private String frequence;
    private boolean effectuer=false;
}
