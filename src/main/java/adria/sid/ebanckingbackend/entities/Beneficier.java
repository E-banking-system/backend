package adria.sid.ebanckingbackend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "beneficier_id")
public class Beneficier extends Personne {
    private String rib;
    private String numCompte;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "beneficier")
    private List<Virement> virements;
}
