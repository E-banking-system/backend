package adria.sid.ebanckingbackend.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Beneficier extends Personne{
    private String RIB;

    @OneToMany(mappedBy = "beneficier", cascade = CascadeType.ALL)
    private List<Virement> virements;

}
