package adria.sid.ebanckingbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import adria.sid.ebanckingbackend.ennumerations.*;

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
    private Date dateExecution;

    @Enumerated(EnumType.STRING)
    private EVType frequence;

    private boolean effectuer=false;
}
