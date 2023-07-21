package adria.sid.ebanckingbackend.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String titre;
    private String contenu;
    private String dateEnvoie;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
