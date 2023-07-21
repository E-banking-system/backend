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
public class Beneficier extends Personne{
    private String RIB;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "beneficier")
    private List<Virement> virements;

    public void addVirement(Virement vrmnt) {
        if (virements == null) {
            virements = new ArrayList<>();
        }
        virements.add(vrmnt);
        vrmnt.setBeneficier(this); // Set the user reference in the notification
    }
}
