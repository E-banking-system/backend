package adria.sid.ebanckingbackend.entities;

import adria.sid.ebanckingbackend.ennumerations.*;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class VirementPermanant extends Virement{
    private Boolean estPermanent=true;

    @Enumerated(EnumType.STRING)
    private EVType frequence;
}
