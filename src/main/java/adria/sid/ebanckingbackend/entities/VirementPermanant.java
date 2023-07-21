package adria.sid.ebanckingbackend.entities;

import adria.sid.ebanckingbackend.ennumerations.*;
import jakarta.persistence.Entity;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class VirementPermanant extends Virement{
    private Boolean estPermanent;
    private Date horaire;
    private EVType virementType;
}
