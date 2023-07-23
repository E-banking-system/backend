package adria.sid.ebanckingbackend.entities;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class NotificationTest {

    @Test
    void testGettersAndSetters() {
        // Test the getters and setters
        String notificationId = UUID.randomUUID().toString();
        Notification notification = new Notification();
        notification.setId(notificationId);
        notification.setTitre("New Message");
        notification.setContenu("Hello, this is a test notification.");
        notification.setDateEnvoie(new Date());

        assertEquals(notificationId, notification.getId());
        assertEquals("New Message", notification.getTitre());
        assertEquals("Hello, this is a test notification.", notification.getContenu());
        assertNotNull(notification.getDateEnvoie());
    }

    @Test
    void testUserAssociation() {
        // Test the association with the User class
        Notification notification = new Notification();
        UserEntity user = new UserEntity();
        String userId = UUID.randomUUID().toString();
        user.setId(userId);

        notification.setUser(user);

        assertEquals(user, notification.getUser());
    }

    @Test
    void testMultipleNotificationsForUser() {
        // Test the association with multiple notifications for a single user
        UserEntity user = new UserEntity();
        String userId = UUID.randomUUID().toString();
        user.setId(userId);

        Notification notification1 = new Notification();
        String notification1Id = UUID.randomUUID().toString();
        notification1.setId(notification1Id);
        notification1.setTitre("Notification 1");
        notification1.setContenu("This is the first notification.");
        notification1.setDateEnvoie(new Date());
        user.addNotification(notification1); // Use addNotification() to add the notification

        Notification notification2 = new Notification();
        String notification2Id = UUID.randomUUID().toString();
        notification2.setId(notification2Id);
        notification2.setTitre("Notification 2");
        notification2.setContenu("This is the second notification.");
        notification2.setDateEnvoie(new Date());
        user.addNotification(notification2); // Use addNotification() to add the notification

        // Check if the notifications are associated with the correct user
        assertEquals(user, notification1.getUser());
        assertEquals(user, notification2.getUser());

        // Check if the user has both notifications
        assertEquals(2, user.getNotifications().size());
        assertTrue(user.getNotifications().contains(notification1));
        assertTrue(user.getNotifications().contains(notification2));
    }
}
