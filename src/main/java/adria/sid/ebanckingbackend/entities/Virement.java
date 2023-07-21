package adria.sid.ebanckingbackend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Virement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private Date dateOperation;

    private Double montant;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "beneficier_id")
    private Beneficier beneficier;

}
