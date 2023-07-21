package adria.sid.ebanckingbackend.entities;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Beneficier extends Personne{
    private String RIB;
}
