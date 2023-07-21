package adria.sid.ebanckingbackend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Beneficier extends Personne{
    private String RIB;

    @OneToMany(mappedBy = "beneficier", cascade = CascadeType.ALL)
    private List<Virement> virements;
}
