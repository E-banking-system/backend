package adria.sid.ebanckingbackend.ennumerations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PERMISSION {

    BANQUIER_SUITE_REGISTRATION_CLIENT("banquier:banquier_suite_registration_client"),
    BANQUIER_READ("banquier:read"),
    BANQUIER_UPDATE("banquier:update"),
    BANQUIER_DELETE("banquier:delete"),
    CLIENT_READ("client:read"),
    CLIENT_UPDATE("client:update"),
    CLIENT_CREATE("client:create"),
    CLIENT_DELETE("client:delete");

    @Getter
    private final String permission;
}
