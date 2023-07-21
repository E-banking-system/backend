package adria.sid.ebanckingbackend.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Compte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String nature;

    private Double sold;

    private String RIB;

    private Long numCompte;

    private Date dateCreation;

    private Date datePeremption;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
