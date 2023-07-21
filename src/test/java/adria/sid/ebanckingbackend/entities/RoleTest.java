package adria.sid.ebanckingbackend.entities;

import adria.sid.ebanckingbackend.ennumerations.EGender;
import adria.sid.ebanckingbackend.ennumerations.EPType;
import adria.sid.ebanckingbackend.ennumerations.ERole;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RoleTest {

    @Test
    void testGettersAndSetters() {
        // Test the getters and setters
        String roleId = UUID.randomUUID().toString();
        Role role = new Role();

        role.setId(roleId);
        role.setName(ERole.CLIENT);

        assertEquals(roleId, role.getId());
        assertEquals("CLIENT", role.getName().toString());
    }

    @Test
    void testUsers() {
        Role role = new Role();

        User user = new User();
        String userId = UUID.randomUUID().toString();

        user.setId(userId);

        role.addUser(user);

        assertEquals(1, role.getUsers().size());
        assertTrue(role.getUsers().contains(user));
    }


}
