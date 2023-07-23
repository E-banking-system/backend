package adria.sid.ebanckingbackend.entities;

import static org.junit.jupiter.api.Assertions.*;

import adria.sid.ebanckingbackend.ennumerations.ERole;
import org.junit.jupiter.api.Test;

class UserEntityTest {

    @Test
    void testAddNotification() {
        UserEntity user = new UserEntity();
        Notification notification = new Notification();
        user.addNotification(notification);

        assertNotNull(user.getNotifications());
        assertEquals(1, user.getNotifications().size());
        assertTrue(user.getNotifications().contains(notification));
        assertEquals(user, notification.getUser());
    }

    @Test
    void testAddCompte() {
        UserEntity user = new UserEntity();
        Compte compte = new Compte();
        user.addCompte(compte);

        assertNotNull(user.getComptes());
        assertEquals(1, user.getComptes().size());
        assertTrue(user.getComptes().contains(compte));
        assertEquals(user, compte.getUser());
    }

    @Test
    void testGetAuthorities() {
        UserEntity user = new UserEntity();
        user.setRole(ERole.USER);

        assertNotNull(user.getAuthorities());
        assertEquals(1, user.getAuthorities().size());
        assertTrue(user.getAuthorities().contains(ERole.USER));
    }

    @Test
    void testGetPassword() {
        UserEntity user = new UserEntity();
        user.setPassword("test-password");

        assertEquals("test-password", user.getPassword());
    }

    @Test
    void testGetUsername() {
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");

        assertEquals("test@example.com", user.getUsername());
    }

    @Test
    void testIsAccountNonExpired() {
        UserEntity user = new UserEntity();

        assertTrue(user.isAccountNonExpired());
    }

    @Test
    void testIsAccountNonLocked() {
        UserEntity user = new UserEntity();

        assertTrue(user.isAccountNonLocked());
    }

    @Test
    void testIsCredentialsNonExpired() {
        UserEntity user = new UserEntity();

        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled() {
        UserEntity user = new UserEntity();

        assertTrue(user.isEnabled());
    }
}
