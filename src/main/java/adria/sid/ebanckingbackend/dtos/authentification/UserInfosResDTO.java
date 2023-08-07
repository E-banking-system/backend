package adria.sid.ebanckingbackend.dtos.authentification;

import adria.sid.ebanckingbackend.ennumerations.EGender;
import adria.sid.ebanckingbackend.ennumerations.EPType;
import adria.sid.ebanckingbackend.ennumerations.ERole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfosResDTO {
    private String id;
    private String nom;
    private String prenom;
    private EGender gender;
    private String operateur;
    private String address;
    private String tel;
    private String cin;
    private String raisonSociale;
    private String registerNumber;
    private EPType personneType;
    private String email;
    private ERole role;
}
