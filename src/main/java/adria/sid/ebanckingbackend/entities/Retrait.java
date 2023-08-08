package adria.sid.ebanckingbackend.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Retrait extends Operation{
    private Boolean estRetrait=true;
}
