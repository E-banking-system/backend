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
    void testUser() {
        Message msg = new Message();
        UserEntity user = new UserEntity();

        String userId = UUID.randomUUID().toString();
        user.setId(userId);

        msg.setSender(user);

        assertEquals(user, msg.getSender());
    }
}
