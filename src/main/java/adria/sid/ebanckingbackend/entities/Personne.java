package adria.sid.ebanckingbackend.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Personne {

    enum EGender {
        MALE,
        FEMALE
    }

    enum EPType {
        MORALE,
        PHYSIQUE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String nom;
    private String prenom;
    private EGender gender;

    private String raisonSociale;
    private String registerNumber;

    private EPType personneType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "banquier_id")
    private Banquier banquier;

}
