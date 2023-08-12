package adria.sid.ebanckingbackend.entities;

import adria.sid.ebanckingbackend.ennumerations.EtatCompte;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Compte {
    @Id
    private String id;
    private String nature;
    private Double solde;
    private String rib;
    private String numCompte;
    private Date dateCreation;
    private Date datePeremption;
    private Date derniereDateSuspention;
    private Date derniereDateBloquage;
    private String codePIN;

    @Enumerated(EnumType.STRING)
    private EtatCompte etatCompte;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "compte", cascade = CascadeType.ALL)
    private List<Operation> operations;

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
