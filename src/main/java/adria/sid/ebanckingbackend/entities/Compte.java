package adria.sid.ebanckingbackend.entities;

import adria.sid.ebanckingbackend.ennumerations.EtatCompte;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Compte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String nature;

    private Double sold;

    private String RIB;

    private Long numCompte;

    private Date dateCreation;

    private Date datePeremption;

    private Date derniereDateSuspention;

    private Date derniereDateBloquage;

    private EtatCompte etatCompte=EtatCompte.BLOCKE;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

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
