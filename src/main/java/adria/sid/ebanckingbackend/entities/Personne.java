package adria.sid.ebanckingbackend.entities;

import adria.sid.ebanckingbackend.ennumerations.*;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "personne_id")
public class Personne {

    @Id
    private String id;

    private String nom;
    private String prenom;
    private EGender gender;

    private String raisonSociale;
    private String registerNumber;

    private EPType personneType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "banquier_id", referencedColumnName = "id")
    private Banquier banquier;

}
