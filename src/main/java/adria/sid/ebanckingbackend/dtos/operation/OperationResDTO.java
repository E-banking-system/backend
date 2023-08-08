package adria.sid.ebanckingbackend.dtos.operation;

import adria.sid.ebanckingbackend.ennumerations.EVType;
import adria.sid.ebanckingbackend.entities.Compte;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationResDTO {
    private String id;
    private Date dateOperation;
    private Double montant;

    @Enumerated(EnumType.STRING)
    private EVType frequence;

    private Boolean estDepot;
    private Boolean estRetrait;
    private Boolean estVirementPermanent;
    private Boolean estVirementUnitaire;
}
