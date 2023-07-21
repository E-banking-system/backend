package adria.sid.ebanckingbackend.entities;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VirementUnitaireTest {

    @Test
    public void testGettersAndSetters(){
        VirementUnitaire vrmnt = new VirementUnitaire();
        String vrmntId = UUID.randomUUID().toString();

        vrmnt.setId(vrmntId);
        Date dateOp = new Date();
        vrmnt.setDateOperation(dateOp);
        vrmnt.setMontant(500.0);
        vrmnt.setEstUnitaire(true);

        assertEquals(vrmntId, vrmnt.getId());
        assertEquals(dateOp, vrmnt.getDateOperation());
        assertEquals(500.0, vrmnt.getMontant());
        assertEquals(true, vrmnt.getEstUnitaire());
    }
}
