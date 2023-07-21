package adria.sid.ebanckingbackend.entities;

import jakarta.persistence.*;
import lombok.Data;

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

}
