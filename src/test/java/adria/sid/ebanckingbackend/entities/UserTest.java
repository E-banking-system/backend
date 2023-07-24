package adria.sid.ebanckingbackend.entities;

import adria.sid.ebanckingbackend.ennumerations.ERole;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testGettersAndSetters() {
        // Test the getters and setters
        String userId = UUID.randomUUID().toString();
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setNom("John");
        user.setPrenom("Doe");
        user.setEmail("johndoe");
        user.setPassword("mypassword");

        assertEquals(userId, user.getId());
        assertEquals("John", user.getNom());
        assertEquals("Doe", user.getPrenom());
        assertEquals("johndoe", user.getUsername());
        assertEquals("mypassword", user.getPassword());
    }

    @Test
    void testNotifications() {
        // Test adding notifications
        UserEntity user = new UserEntity();

        Notification notification1 = new Notification();
        String notification1Id = UUID.randomUUID().toString();
        notification1.setId(notification1Id);
        notification1.setContenu("eorijieorjg");
        notification1.setDateEnvoie(new Date());
        notification1.setTitre("titre1");

        Notification notification2 = new Notification();
        String notification2Id = UUID.randomUUID().toString();
        notification2.setId(notification2Id);
        notification2.setContenu("eorijieorjg");
        notification2.setDateEnvoie(new Date());
        notification2.setTitre("titre2");

        user.addNotification(notification1);
        user.addNotification(notification2);

        assertEquals(2, user.getNotifications().size());
        assertTrue(user.getNotifications().contains(notification1));
        assertTrue(user.getNotifications().contains(notification2));
    }

    @Test
    void testComptes() {
        // Test adding comptes
        UserEntity user = new UserEntity();

        Compte compte1 = new Compte();
        String compte1Id = UUID.randomUUID().toString();
        compte1.setId(compte1Id);
        compte1.setRIB("8754218974561289745");
        compte1.setNature("cheque");
        compte1.setDatePeremption(new Date());
        compte1.setDateCreation(new Date());
        compte1.setSolde(8745.0);

        Compte compte2 = new Compte();
        String compte2Id = UUID.randomUUID().toString();
        compte2.setId(compte2Id);
        compte2.setRIB("8754218974561289745");
        compte2.setNature("cheque");
        compte2.setDatePeremption(new Date());
        compte2.setDateCreation(new Date());
        compte2.setSolde(8745.0);

        user.addCompte(compte1);
        user.addCompte(compte2);

        assertEquals(2, user.getComptes().size());
        assertTrue(user.getComptes().contains(compte1));
        assertTrue(user.getComptes().contains(compte2));
    }

}
