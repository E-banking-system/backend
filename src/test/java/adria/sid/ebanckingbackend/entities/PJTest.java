package adria.sid.ebanckingbackend.entities;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PJTest {
    @Test
    void testGettersAndSetters() {
        // Test the getters and setters
        String pjId = UUID.randomUUID().toString();
        PJ pj = new PJ();
        pj.setId(pjId);
        pj.setName("pj");

        assertEquals(pjId, pj.getId());
        assertEquals("pj", pj.getName());
    }

    @Test
    void testMessage(){
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
}
