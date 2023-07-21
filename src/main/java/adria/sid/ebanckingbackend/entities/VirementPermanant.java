package adria.sid.ebanckingbackend.entities;

import jakarta.persistence.Entity;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
public class VirementPermanant extends Virement{

    enum EVType{
        HEBDOMADAIRE,
        BIMENSUELLE,
        MENSUELLE,
        TRIMESTRIELLE,
        SEMESTRIELLE
    }

    private List<Beneficier> listBeneficiers;

    private Date horaire;

    private EVType virementType;

}
