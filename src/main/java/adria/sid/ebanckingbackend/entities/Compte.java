package adria.sid.ebanckingbackend.entities;

import adria.sid.ebanckingbackend.ennumerations.EtatCompte;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "compte_id")
public class Compte {

    @Id
    private String id;

    private String nature;

    private Double solde;

    private String RIB;

    private Long numCompte;

    private Date dateCreation;

    private Date datePeremption;

    private Date derniereDateSuspention;

    private Date derniereDateBloquage;

    private EtatCompte etatCompte=EtatCompte.BLOCKE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public void activerCompte() {
        this.setEtatCompte(EtatCompte.ACTIVE);
    }

    public void blockerCompte() {
        this.setEtatCompte(EtatCompte.BLOCKE);
    }

    public void suspenduCompte(){
        this.setEtatCompte(EtatCompte.SUSPENDU);
    }
}
