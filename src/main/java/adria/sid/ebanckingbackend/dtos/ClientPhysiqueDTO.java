package adria.sid.ebanckingbackend.dtos;

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
public class ClientPhysiqueDTO {

  @NotBlank(message = "Nom is required")
  private String nom;

  @NotBlank(message = "Prénom is required")
  private String prenom;

  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format")
  private String email;

  @NotBlank(message = "Gender is required")
  private String gender;

  @NotBlank(message = "Adresse is required")
  private String address;

  @Pattern(regexp = "[A-Za-z]{2}\\d{6}", message = "CIN must be 2 letters followed by 6 digits")
  private String cin;

  @Pattern(regexp = "\\d{10}", message = "Telephone must be 10 digits")
  private String tel;

  @NotBlank(message = "Operateur is required")
  private String operateur;

  @NotBlank(message = "Password is required")
  private String password;

}
