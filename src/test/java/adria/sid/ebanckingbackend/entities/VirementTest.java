package adria.sid.ebanckingbackend.entities;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VirementTest {

    @Test
    public void testGettersAndSetters(){
        Virement vrmnt = new Virement();
        String vrmntId = UUID.randomUUID().toString();

        vrmnt.setId(vrmntId);
        Date dateOp = new Date();
        vrmnt.setDateOperation(dateOp);
        vrmnt.setMontant(500.0);

        assertEquals(vrmntId, vrmnt.getId());
        assertEquals(dateOp, vrmnt.getDateOperation());
        assertEquals(500.0, vrmnt.getMontant());
    }

    /*@Test
    void testClient() {
        Virement vrmnt = new Virement();
        UserEntity clt = new UserEntity();

        String clientId = UUID.randomUUID().toString();
        clt.setId(clientId);

        vrmnt.setUser(clt);

        assertEquals(clt, vrmnt.getUser());
    }*/

    @Test
    void testBeneficier() {
        Virement vrmnt = new Virement();
        Beneficier b = new Beneficier();

        String bId = UUID.randomUUID().toString();

        vrmnt.setBeneficier(b);

        assertEquals(b, vrmnt.getBeneficier());
    }
}
