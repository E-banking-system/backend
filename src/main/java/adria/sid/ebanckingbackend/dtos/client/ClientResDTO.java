package adria.sid.ebanckingbackend.dtos.client;

import adria.sid.ebanckingbackend.ennumerations.EGender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientResDTO {
  private String id;
  private String nom;
  private String prenom;
  private String email;
  private EGender gender;
  private String address;
  private String cin;
  private String tel;
  private String operateur;
}
