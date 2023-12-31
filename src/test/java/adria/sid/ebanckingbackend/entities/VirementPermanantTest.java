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
        Date horaire = new Date();

        assertEquals(vrmntId, vrmnt.getId());
        assertEquals(dateOp, vrmnt.getDateOperation());
        assertEquals(500.0, vrmnt.getMontant());
    }
}
