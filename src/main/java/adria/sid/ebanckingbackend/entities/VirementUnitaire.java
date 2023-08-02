package adria.sid.ebanckingbackend.entities;

import adria.sid.ebanckingbackend.ennumerations.TokenType;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class VirementUnitaire extends Virement{
    private Boolean estUnitaire=true;
}
