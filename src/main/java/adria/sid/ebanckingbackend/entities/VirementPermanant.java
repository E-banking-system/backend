package adria.sid.ebanckingbackend.entities;

import adria.sid.ebanckingbackend.ennumerations.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class VirementPermanant extends Virement{
    @JsonIgnore
    private Boolean estPermanent=true;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private EVType frequence;
}
