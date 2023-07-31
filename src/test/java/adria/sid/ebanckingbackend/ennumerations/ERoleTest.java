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
        assertEquals(Set.of(PERMISSION.BANQUIER_SUITE_REGISTRATION_CLIENT, PERMISSION.CHANGE_SOLDE, PERMISSION.GET_ACCOUNTS, PERMISSION.ACTIVER_ACCOUNT, PERMISSION.BLOCK_ACCOUNT, PERMISSION.SUSPENDER_ACCOUNT), banquier.getPermissions());
        assertEquals(Set.of(PERMISSION.DEMANDE_ACTIVATE_COMPTE, PERMISSION.GET_CLIENT_COMPTES, PERMISSION.DEMANDE_BLOCK_COMPTE, PERMISSION.DEMANDE_SUSPEND_COMPTE), client.getPermissions());
    }

    @Test
    void testGetAuthorities() {
        ERole user = ERole.USER;
        ERole banquier = ERole.BANQUIER;
        ERole client = ERole.CLIENT;

        assertEquals(1, user.getAuthorities().size());
        assertTrue(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));

        assertEquals(5, banquier.getAuthorities().size());
        assertTrue(banquier.getAuthorities().contains(new SimpleGrantedAuthority("banquier:read")));
        assertTrue(banquier.getAuthorities().contains(new SimpleGrantedAuthority("banquier:update")));
        assertTrue(banquier.getAuthorities().contains(new SimpleGrantedAuthority("banquier:delete")));
        assertTrue(banquier.getAuthorities().contains(new SimpleGrantedAuthority("banquier:create")));
        assertTrue(banquier.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_BANQUIER")));

        assertEquals(5, client.getAuthorities().size());
        assertTrue(client.getAuthorities().contains(new SimpleGrantedAuthority("client:read")));
        assertTrue(client.getAuthorities().contains(new SimpleGrantedAuthority("client:update")));
        assertTrue(client.getAuthorities().contains(new SimpleGrantedAuthority("client:delete")));
        assertTrue(client.getAuthorities().contains(new SimpleGrantedAuthority("client:create")));
        assertTrue(client.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CLIENT")));
    }

}
