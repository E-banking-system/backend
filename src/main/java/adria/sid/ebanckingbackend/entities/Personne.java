package adria.sid.ebanckingbackend.entities;

import adria.sid.ebanckingbackend.ennumerations.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @Enumerated(EnumType.STRING)
    private EGender gender;

    private String operateur;
    private String address;
    private String tel;
    private String cin;
    private String raisonSociale;
    private String registerNumber;

    @Enumerated(EnumType.STRING)
    private EPType personneType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "related_personne_id")
    private Personne relatedPersonne;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "client") // Change to FetchType.EAGER
    private List<Beneficier> beneficiers;
}
