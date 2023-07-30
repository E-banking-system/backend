package adria.sid.ebanckingbackend.ennumerations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PERMISSION {

    BANQUIER_SUITE_REGISTRATION_CLIENT("banquier:banquier_suite_registration_client"),
    GET_ACCOUNTS("banquier:get_accounts"),
    BLOCK_ACCOUNT("banquier:blocker_compte"),
    ACTIVER_ACCOUNT("banquier:activer_compte"),
    SUSPENDER_ACCOUNT("banquier:suspender_compte"),
    CHANGE_SOLDE("banquier:change_solde"),
    GET_CLIENT_COMPTES("client:get_client_comptes"),
    GET_NOTIFICATIONS_BY_USER_ID("banquier:get_notification");

    @Getter
    private final String permission;
}
