package adria.sid.ebanckingbackend.entities;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClientTest {

    @Test
    void testGettersAndSetters() {
        // Test the getters and setters
        String clientId = UUID.randomUUID().toString();
        Client clt = new Client();
        clt.setId(clientId);
        clt.setNom("kaoutar");
        clt.setPrenom("sougrati");
        clt.setUserName("kaokao");
        clt.setPassword("1234");
        clt.setCIN("JC617191");

        assertEquals(clientId, clt.getId());
        assertEquals("kaoutar", clt.getNom());
        assertEquals("sougrati", clt.getPrenom());
        assertEquals("kaokao", clt.getUserName());
        assertEquals("1234", clt.getPassword());
        assertEquals("JC617191", clt.getCIN());
    }

    @Test
    void testMessages() {
        Client clt = new Client();
        Message msg = new Message();
        String msgId = UUID.randomUUID().toString();

        msg.setId(msgId);
        msg.setMessage("bonjour!");
        msg.setIsRead(false);

        clt.addMsg(msg);

        assertEquals(1, clt.getMessages().size());
        assertTrue(clt.getMessages().contains(msg));
        assertTrue(clt.getMessages().contains(msg));

    }

    @Test
    void testVirement() {
        Client clt = new Client();

        Virement virement = new Virement();
        String virementId = UUID.randomUUID().toString();

        virement.setId(virementId);
        virement.setMontant(2000.0);
        virement.setDateOperation(new Date());

        clt.addVirement(virement);

        assertEquals(1, clt.getVirements().size());
        assertTrue(clt.getVirements().contains(virement));

    }

}
