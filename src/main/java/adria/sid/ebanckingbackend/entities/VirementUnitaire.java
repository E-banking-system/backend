package adria.sid.ebanckingbackend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VirementUnitaire extends Virement{
    private Boolean estUnitaire=true;
}
