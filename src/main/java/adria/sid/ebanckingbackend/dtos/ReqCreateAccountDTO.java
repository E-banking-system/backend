package adria.sid.ebanckingbackend.dtos;

import adria.sid.ebanckingbackend.ennumerations.EtatCompte;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReqCreateAccountDTO {

    private String nature;
    private Double solde;
    private String RIB;
    private Long numCompte;
    private Date dateCreation;
    private Date datePeremption;
    private Date derniereDateSuspention;
    private Date derniereDateBloquage;
    private EtatCompte etatCompte;
}
