package adria.sid.ebanckingbackend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Beneficier {
    @Id
    private String beneficier_id;

    private String numCompte;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "beneficier")
    private List<Virement> virements;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_user_id")
    private UserEntity parent_user;
}
