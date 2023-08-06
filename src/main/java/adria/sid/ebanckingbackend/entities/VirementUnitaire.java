package adria.sid.ebanckingbackend.entities;

import adria.sid.ebanckingbackend.ennumerations.TokenType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class VirementUnitaire extends Virement{
    @JsonIgnore
    private Boolean estUnitaire=true;
}
