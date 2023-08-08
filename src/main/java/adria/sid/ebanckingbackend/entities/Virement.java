package adria.sid.ebanckingbackend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Virement extends Operation{
    @ManyToOne
    @JoinColumn(name = "beneficier_id")
    private Beneficier beneficier;
}
