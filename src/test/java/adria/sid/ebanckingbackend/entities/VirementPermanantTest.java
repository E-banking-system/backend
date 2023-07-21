package adria.sid.ebanckingbackend.entities;

import adria.sid.ebanckingbackend.ennumerations.EVType;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VirementPermanantTest {

    @Test
    public void testGettersAndSetters(){
        VirementPermanant vrmnt = new VirementPermanant();
        String vrmntId = UUID.randomUUID().toString();

        vrmnt.setId(vrmntId);
        Date dateOp = new Date();
        vrmnt.setDateOperation(dateOp);
        vrmnt.setMontant(500.0);
        vrmnt.setEstPermanent(true);
        Date horaire = new Date();
        vrmnt.setHoraire(horaire);
        vrmnt.setVirementType(EVType.BIMENSUELLE);

        assertEquals(vrmntId, vrmnt.getId());
        assertEquals(dateOp, vrmnt.getDateOperation());
        assertEquals(500.0, vrmnt.getMontant());
        assertEquals(true, vrmnt.getEstPermanent());
        assertEquals(horaire, vrmnt.getHoraire());
        assertEquals(EVType.BIMENSUELLE, vrmnt.getVirementType());
    }
}
