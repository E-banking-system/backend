package adria.sid.ebanckingbackend.ennumerations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PERMISSION {

    BANQUIER_SUITE_REGISTRATION_CLIENT("banquier:banquier_suite_registration_client"),
    GET_ACCOUNTS("banquier:get_accounts"),
    BLOCK_ACCOUNT("banquier:blocker_compte"),
    ACTIVER_ACCOUNT("banquier:creer_compte"),
    SUSPENDER_ACCOUNT("banquier:suspender_compte"),
    CHANGE_SOLDE("banquier:change_solde"),
    GET_CLIENT_COMPTES("client:get_client_comptes"),
    GET_NOTIFICATIONS_BY_USER_ID("banquier:get_notification"),
    DEMANDE_SUSPEND_COMPTE("client:demande_suspend_compte"),
    DEMANDE_BLOCK_COMPTE("client:demande_block_compte"),
    DEMANDE_ACTIVATE_COMPTE("client:demande_activer_compte");

    @Getter
    private final String permission;
}
