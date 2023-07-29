package adria.sid.ebanckingbackend.ennumerations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PERMISSION {

    BANQUIER_SUITE_REGISTRATION_CLIENT("banquier:banquier_suite_registration_client"),
    GET_ACCOUNTS("banquier:get_accounts");

    @Getter
    private final String permission;
}
