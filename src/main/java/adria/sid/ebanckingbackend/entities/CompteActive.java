package adria.sid.ebanckingbackend.entities;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompteActive extends Compte{
    private Boolean estActive;
}
