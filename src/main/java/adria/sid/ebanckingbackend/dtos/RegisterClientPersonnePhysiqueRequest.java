package adria.sid.ebanckingbackend.dtos;

import adria.sid.ebanckingbackend.ennumerations.EGender;
import adria.sid.ebanckingbackend.ennumerations.EPType;
import adria.sid.ebanckingbackend.ennumerations.ERole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterClientPersonnePhysiqueRequest {

  private String nom;
  private String prenom;
  private String rib;
  private String email;
  private String gender;
  private String adresse;
  private String cin;
  private String telephone;
  private String operateur;
  private String epType;
  private String password;
  private String role;
}
