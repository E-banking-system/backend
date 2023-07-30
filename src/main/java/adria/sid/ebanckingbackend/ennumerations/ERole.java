package adria.sid.ebanckingbackend.ennumerations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static adria.sid.ebanckingbackend.ennumerations.PERMISSION.*;

@RequiredArgsConstructor
public enum ERole {

    USER(Collections.emptySet()),
    BANQUIER(
            Set.of(
                    BANQUIER_SUITE_REGISTRATION_CLIENT,
                    GET_ACCOUNTS,
                    BLOCK_ACCOUNT,
                    ACTIVER_ACCOUNT,
                    SUSPENDER_ACCOUNT,
                    CHANGE_SOLDE,
                    GET_CLIENT_COMPTES,
                    GET_NOTIFICATIONS_BY_USER_ID
            )
    ),
    CLIENT(
            Set.of(
                    GET_CLIENT_COMPTES,
                    GET_NOTIFICATIONS_BY_USER_ID
            )
    );

    @Getter
    private final Set<PERMISSION> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }

}

