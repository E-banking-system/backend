package adria.sid.ebanckingbackend.ennumerations;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.Set;

class ERoleTest {

    @Test
    void testEnumValues() {
        ERole user = ERole.USER;
        ERole banquier = ERole.BANQUIER;
        ERole client = ERole.CLIENT;

        assertNotNull(user);
        assertNotNull(banquier);
        assertNotNull(client);
    }

    @Test
    void testEnumPermissions() {
        ERole user = ERole.USER;
        ERole banquier = ERole.BANQUIER;
        ERole client = ERole.CLIENT;

        assertEquals(Collections.emptySet(), user.getPermissions());
        assertEquals(Set.of(PERMISSION.BANQUIER_READ, PERMISSION.BANQUIER_UPDATE, PERMISSION.BANQUIER_DELETE, PERMISSION.BANQUIER_CREATE), banquier.getPermissions());
        assertEquals(Set.of(PERMISSION.CLIENT_READ, PERMISSION.CLIENT_UPDATE, PERMISSION.CLIENT_DELETE, PERMISSION.CLIENT_CREATE), client.getPermissions());
    }

    @Test
    void testGetAuthorities() {
        ERole user = ERole.USER;
        ERole banquier = ERole.BANQUIER;
        ERole client = ERole.CLIENT;

        assertEquals(1, user.getAuthorities().size());
        assertTrue(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));

        assertEquals(5, banquier.getAuthorities().size());
        assertTrue(banquier.getAuthorities().contains(new SimpleGrantedAuthority("BANQUIER_READ")));
        assertTrue(banquier.getAuthorities().contains(new SimpleGrantedAuthority("BANQUIER_UPDATE")));
        assertTrue(banquier.getAuthorities().contains(new SimpleGrantedAuthority("BANQUIER_DELETE")));
        assertTrue(banquier.getAuthorities().contains(new SimpleGrantedAuthority("BANQUIER_CREATE")));
        assertTrue(banquier.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_BANQUIER")));

        assertEquals(5, client.getAuthorities().size());
        assertTrue(client.getAuthorities().contains(new SimpleGrantedAuthority("CLIENT_READ")));
        assertTrue(client.getAuthorities().contains(new SimpleGrantedAuthority("CLIENT_UPDATE")));
        assertTrue(client.getAuthorities().contains(new SimpleGrantedAuthority("CLIENT_DELETE")));
        assertTrue(client.getAuthorities().contains(new SimpleGrantedAuthority("CLIENT_CREATE")));
        assertTrue(client.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CLIENT")));
    }
}
