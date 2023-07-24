package adria.sid.ebanckingbackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import jakarta.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReqRegisterBanquierDTO {

  @NotBlank(message = "Nom is required")
  private String nom;

  @NotBlank(message = "Prenom is required")
  private String prenom;

  @Email(message = "Invalid email format")
  @NotBlank(message = "Email is required")
  private String email;

  @NotBlank(message = "Gender is required")
  private String gender;

  @NotBlank(message = "Adresse is required")
  private String adresse;

  @Pattern(regexp = "[A-Za-z]{2}\\d{6}", message = "CIN must be 2 letters followed by 6 digits")
  @NotBlank(message = "CIN is required")
  private String cin;

  @Pattern(regexp = "\\d{10}", message = "Telephone must be 10 digits")
  @NotBlank(message = "Telephone is required")
  private String telephone;

  @NotBlank(message = "Operateur is required")
  private String operateur;

  @NotBlank(message = "BanqueId is required")
  private String banqueId;

  @NotBlank(message = "Password is required")
  private String password;

  @NotBlank(message = "Role is required")
  private String role;
}
