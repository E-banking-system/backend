package adria.sid.ebanckingbackend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends Personne{
    private String userName;
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Notification> notifications;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Compte> comptes;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

}
