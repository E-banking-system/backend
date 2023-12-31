package adria.sid.ebanckingbackend.entities;

import adria.sid.ebanckingbackend.ennumerations.ERole;
import adria.sid.ebanckingbackend.security.accessToken.Token;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
@Getter
@Setter
public class UserEntity extends Personne implements UserDetails {
    private String email;
    private String password;
    private Boolean enabled=false;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Notification> notifications;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<Beneficier> beneficiers;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private List<Compte> comptes;

    @Enumerated(EnumType.STRING)
    private ERole role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sender")
    private List<Message> sentMessages; // Messages sent by the user

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "receiver")
    private List<Message> receivedMessages; // Messages received by the user

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Token> tokens;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Add a method to add notifications to the list safely
    public void addNotification(Notification notification) {
        if (notifications == null) {
            notifications = new ArrayList<>();
        }
        notifications.add(notification);
        notification.setUser(this); // Set the user reference in the notification
    }

    public void addCompte(Compte compte) {
        if (comptes == null) {
            comptes = new ArrayList<>();
        }
        comptes.add(compte);
        compte.setUser(this); // Set the user reference in the notification
    }
}
