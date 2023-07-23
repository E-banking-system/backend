package adria.sid.ebanckingbackend.entities;

import adria.sid.ebanckingbackend.ennumerations.*;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@PrimaryKeyJoinColumn(name = "personne_id")
public class Personne {

    @Id
    private String id;

    private String nom;
    private String prenom;
    private EGender gender;
    private String operateur;
    private String address;
    private String RIB;
    private String tel;
    private String CIN;

    private String raisonSociale;
    private String registerNumber;

    private EPType personneType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "personne_id")
    private Personne personne;
}
