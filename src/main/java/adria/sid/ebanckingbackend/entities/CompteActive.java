package adria.sid.ebanckingbackend.entities;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class CompteActive extends Compte{
    private Boolean estActive;
}
