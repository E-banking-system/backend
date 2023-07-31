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
        assertEquals(Set.of(PERMISSION.GET_NOTIFICATIONS_BY_USER_ID,PERMISSION.BANQUIER_SUITE_REGISTRATION_CLIENT, PERMISSION.CHANGE_SOLDE, PERMISSION.GET_ACCOUNTS, PERMISSION.ACTIVER_ACCOUNT, PERMISSION.BLOCK_ACCOUNT, PERMISSION.SUSPENDER_ACCOUNT), banquier.getPermissions());
        assertEquals(Set.of(PERMISSION.DEMANDE_ACTIVATE_COMPTE, PERMISSION.GET_CLIENT_COMPTES, PERMISSION.DEMANDE_BLOCK_COMPTE, PERMISSION.DEMANDE_SUSPEND_COMPTE), client.getPermissions());
    }

    @Test
    void testGetAuthorities() {
        ERole user = ERole.USER;
        ERole banquier = ERole.BANQUIER;
        ERole client = ERole.CLIENT;

        assertEquals(1, user.getAuthorities().size());
        assertTrue(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));

        assertEquals(8, banquier.getAuthorities().size());
        assertTrue(banquier.getAuthorities().contains(new SimpleGrantedAuthority("banquier:banquier_suite_registration_client")));
        assertTrue(banquier.getAuthorities().contains(new SimpleGrantedAuthority("banquier:get_accounts")));
        assertTrue(banquier.getAuthorities().contains(new SimpleGrantedAuthority("banquier:blocker_compte")));
        assertTrue(banquier.getAuthorities().contains(new SimpleGrantedAuthority("banquier:activer_compte")));
        assertTrue(banquier.getAuthorities().contains(new SimpleGrantedAuthority("banquier:suspender_compte")));
        assertTrue(banquier.getAuthorities().contains(new SimpleGrantedAuthority("banquier:change_solde")));
        assertTrue(banquier.getAuthorities().contains(new SimpleGrantedAuthority("banquier:get_notification")));

        assertEquals(5, client.getAuthorities().size());
        assertTrue(client.getAuthorities().contains(new SimpleGrantedAuthority("client:get_client_comptes")));
        assertTrue(client.getAuthorities().contains(new SimpleGrantedAuthority("client:demande_suspend_compte")));
        assertTrue(client.getAuthorities().contains(new SimpleGrantedAuthority("client:demande_block_compte")));
        assertTrue(client.getAuthorities().contains(new SimpleGrantedAuthority("client:demande_activer_compte")));
    }

}
