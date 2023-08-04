package adria.sid.ebanckingbackend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "beneficier_id")
public class Beneficier extends Personne {

    private String numCompte;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Personne client;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "beneficier")
    private List<Virement> virements;

    /*public void addVirement(Virement virement) {
        if (virements == null) {
            virements = new ArrayList<>();
        }
        virements.add(virement);
        virement.setBeneficier(this);
    }*/

    @Override
    public String toString() {
        return "Beneficier{" +
                "id='" + getId() + '\'' +
                ", nom='" + getNom() + '\'' +
                ", prenom='" + getPrenom() + '\'' +
                ", RIB='" + numCompte + '\'' +
                '}';
    }
}
