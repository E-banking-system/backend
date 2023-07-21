package adria.sid.ebanckingbackend.entities;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BanquierTest {

    @Test
    void testGettersAndSetters() {
        // Test the getters and setters
        String baquierID = UUID.randomUUID().toString();
        Banquier banquier = new Banquier();
        banquier.setId(baquierID);
        banquier.setNom("kaoutar");
        banquier.setPrenom("sougrati");
        banquier.setUserName("kaokao");
        banquier.setPassword("1234");

        assertEquals(baquierID, banquier.getId());
        assertEquals("kaoutar", banquier.getNom());
        assertEquals("sougrati", banquier.getPrenom());
        assertEquals("kaokao", banquier.getUserName());
        assertEquals("1234", banquier.getPassword());
    }

    @Test
    void testMessages() {
        Banquier banquier = new Banquier();
        Message msg = new Message();
        String msgId = UUID.randomUUID().toString();

        msg.setId(msgId);
        msg.setMessage("bonjour!");
        msg.setIsRead(false);

        banquier.addMsg(msg);

        assertEquals(1, banquier.getMessages().size());
        assertTrue(banquier.getMessages().contains(msg));
        assertTrue(banquier.getMessages().contains(msg));

    }

    @Test
    void testPersonne(){
        Banquier banquier = new Banquier();
        String baquierID = UUID.randomUUID().toString();
        banquier.setId(baquierID);
        banquier.setNom("kaoutar");
        banquier.setPrenom("sougrati");
        banquier.setUserName("kaokao");
        banquier.setPassword("1234");

        Personne personne = new Personne();
        String personneID = UUID.randomUUID().toString();
        personne.setId(personneID);
        personne.setNom("p1");
        personne.setPrenom("p1");

        personne.setBanquier(banquier);
        banquier.setPersonne(personne);

        assertEquals(personne, banquier.getPersonne());
        assertEquals(banquier, personne.getBanquier());
    }
}
