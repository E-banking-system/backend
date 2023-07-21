package adria.sid.ebanckingbackend.entities;

import adria.sid.ebanckingbackend.ennumerations.ERole;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageTest {

    @Test
    public void testGettersAndSetters(){
        Message msg = new Message();
        String msgId = UUID.randomUUID().toString();

        msg.setId(msgId);
        msg.setMessage("Bonjour!");
        msg.setIsRead(false);

        assertEquals(msgId, msg.getId());
        assertEquals("Bonjour!", msg.getMessage());
        assertEquals(false, msg.getIsRead());
    }

    @Test
    void testPJ(){
        PJ pj = new PJ();
        String pjId = UUID.randomUUID().toString();
        pj.setId(pjId);
        pj.setName("pj");

        Message msg = new Message();
        String msgId = UUID.randomUUID().toString();
        msg.setId(msgId);
        msg.setMessage("bonjour!");
        msg.setIsRead(false);

        pj.setMessage(msg);
        msg.setPj(pj);

        assertEquals(pj, msg.getPj());
        assertEquals(msg, pj.getMessage());
    }

    @Test
    void testBanquier() {
        Message msg = new Message();
        Banquier banquier = new Banquier();

        String roleId = UUID.randomUUID().toString();
        banquier.setId(roleId);

        msg.setBanquier(banquier);

        assertEquals(banquier, msg.getBanquier());
    }

    @Test
    void testClient() {
        Message msg = new Message();
        Client clt = new Client();

        String clientId = UUID.randomUUID().toString();
        clt.setId(clientId);

        msg.setClient(clt);

        assertEquals(clt, msg.getClient());
    }
}
