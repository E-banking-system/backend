package adria.sid.ebanckingbackend.entities;

import adria.sid.ebanckingbackend.ennumerations.*;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Inheritance(strategy = InheritanceType.JOINED)
public class Personne {

    @Id
    private String id;
    private String nom;
    private String prenom;
    private String operateur;
    private String address;
    private String tel;
    private String cin;
    private String raisonSociale;
    private String registerNumber;

    @Enumerated(EnumType.STRING)
    private EPType personneType;

    @Enumerated(EnumType.STRING)
    private EGender gender;
}
