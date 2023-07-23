package adria.sid.ebanckingbackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReqRegisterBanquierDTO {
  private String nom;
  private String prenom;
  private String email;
  private String gender;
  private String adresse;
  private String cin;
  private String telephone;
  private String operateur;
  private String banqueId;
  private String password;
  private String role;
}
