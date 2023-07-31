package adria.sid.ebanckingbackend.ennumerations;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PERMISSIONTest {

    @Test
    void testEnumValues() {
        PERMISSION registrationBanquier = PERMISSION.BANQUIER_SUITE_REGISTRATION_CLIENT;
        PERMISSION changementSoldeBanquier = PERMISSION.CHANGE_SOLDE;
        PERMISSION accBanquier = PERMISSION.GET_ACCOUNTS;
        PERMISSION activerCptBanquier = PERMISSION.ACTIVER_ACCOUNT;
        PERMISSION blockCptBanquier = PERMISSION.BLOCK_ACCOUNT;
        PERMISSION suspendCptBanquier = PERMISSION.SUSPENDER_ACCOUNT;
        PERMISSION notificationsBanquier = PERMISSION.GET_NOTIFICATIONS_BY_USER_ID;

        PERMISSION demandeActivationClient = PERMISSION.DEMANDE_ACTIVATE_COMPTE;
        PERMISSION getAccsClient = PERMISSION.GET_CLIENT_COMPTES;
        PERMISSION demandeBlockClient = PERMISSION.DEMANDE_BLOCK_COMPTE;
        PERMISSION demandeSuspentionClient = PERMISSION.DEMANDE_SUSPEND_COMPTE;

        assertNotNull(registrationBanquier);
        assertNotNull(changementSoldeBanquier);
        assertNotNull(accBanquier);
        assertNotNull(activerCptBanquier);
        assertNotNull(blockCptBanquier);
        assertNotNull(suspendCptBanquier);
        assertNotNull(notificationsBanquier);
        assertNotNull(demandeActivationClient);
        assertNotNull(getAccsClient);
        assertNotNull(demandeBlockClient);
        assertNotNull(demandeSuspentionClient);
    }

    @Test
    void testEnumPermissions() {
        PERMISSION registrationBanquier = PERMISSION.BANQUIER_SUITE_REGISTRATION_CLIENT;
        PERMISSION changementSoldeBanquier = PERMISSION.CHANGE_SOLDE;
        PERMISSION accBanquier = PERMISSION.GET_ACCOUNTS;
        PERMISSION activerCptBanquier = PERMISSION.ACTIVER_ACCOUNT;
        PERMISSION blockCptBanquier = PERMISSION.BLOCK_ACCOUNT;
        PERMISSION suspendCptBanquier = PERMISSION.SUSPENDER_ACCOUNT;
        PERMISSION notificationsBanquier = PERMISSION.GET_NOTIFICATIONS_BY_USER_ID;

        PERMISSION demandeActivationClient = PERMISSION.DEMANDE_ACTIVATE_COMPTE;
        PERMISSION getAccsClient = PERMISSION.GET_CLIENT_COMPTES;
        PERMISSION demandeBlockClient = PERMISSION.DEMANDE_BLOCK_COMPTE;
        PERMISSION demandeSuspentionClient = PERMISSION.DEMANDE_SUSPEND_COMPTE;

        assertEquals("banquier:banquier_suite_registration_client", registrationBanquier.getPermission());
        assertEquals("banquier:get_accounts", accBanquier.getPermission());
        assertEquals("banquier:blocker_compte", blockCptBanquier.getPermission());
        assertEquals("banquier:activer_compte", activerCptBanquier.getPermission());
        assertEquals("banquier:suspender_compte", suspendCptBanquier.getPermission());
        assertEquals("banquier:change_solde", changementSoldeBanquier.getPermission());
        assertEquals("banquier:get_notification", notificationsBanquier.getPermission());
        assertEquals("client:demande_activer_compte", demandeActivationClient.getPermission());
        assertEquals("client:get_client_comptes", getAccsClient.getPermission());
        assertEquals("client:demande_block_compte", demandeBlockClient.getPermission());
        assertEquals("client:demande_suspend_compte", demandeSuspentionClient.getPermission());
    }

    @Test
    void testEnumToString() {
        PERMISSION registrationBanquier = PERMISSION.BANQUIER_SUITE_REGISTRATION_CLIENT;
        PERMISSION changementSoldeBanquier = PERMISSION.CHANGE_SOLDE;
        PERMISSION accBanquier = PERMISSION.GET_ACCOUNTS;
        PERMISSION activerCptBanquier = PERMISSION.ACTIVER_ACCOUNT;
        PERMISSION blockCptBanquier = PERMISSION.BLOCK_ACCOUNT;
        PERMISSION suspendCptBanquier = PERMISSION.SUSPENDER_ACCOUNT;
        PERMISSION notificationsBanquier = PERMISSION.GET_NOTIFICATIONS_BY_USER_ID;

        PERMISSION demandeActivationClient = PERMISSION.DEMANDE_ACTIVATE_COMPTE;
        PERMISSION getAccsClient = PERMISSION.GET_CLIENT_COMPTES;
        PERMISSION demandeBlockClient = PERMISSION.DEMANDE_BLOCK_COMPTE;
        PERMISSION demandeSuspentionClient = PERMISSION.DEMANDE_SUSPEND_COMPTE;

        assertEquals("BANQUIER_SUITE_REGISTRATION_CLIENT", registrationBanquier.toString());
        assertEquals("GET_ACCOUNTS", accBanquier.toString());
        assertEquals("BLOCK_ACCOUNT", blockCptBanquier.toString());
        assertEquals("ACTIVER_ACCOUNT", activerCptBanquier.toString());
        assertEquals("SUSPENDER_ACCOUNT", suspendCptBanquier.toString());
        assertEquals("CHANGE_SOLDE", changementSoldeBanquier.toString());
        assertEquals("GET_NOTIFICATIONS_BY_USER_ID", notificationsBanquier.toString());
        assertEquals("DEMANDE_ACTIVATE_COMPTE", demandeActivationClient.toString());
        assertEquals("GET_CLIENT_COMPTES", getAccsClient.toString());
        assertEquals("DEMANDE_BLOCK_COMPTE", demandeBlockClient.toString());
        assertEquals("DEMANDE_SUSPEND_COMPTE", demandeSuspentionClient.toString());
    }

}
