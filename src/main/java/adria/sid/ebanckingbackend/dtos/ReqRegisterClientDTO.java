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
public class ReqRegisterClientDTO {

  @NotBlank(message = "Nom is required")
  private String nom;

  @NotBlank(message = "Prénom is required")
  private String prenom;

  @NotBlank(message = "RIB is required")
  private String rib;

  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format")
  private String email;

  @NotBlank(message = "Gender is required")
  private String gender;

  @NotBlank(message = "Adresse is required")
  private String adresse;

  @Pattern(regexp = "[A-Za-z]{2}\\d{6}", message = "CIN must be 2 letters followed by 6 digits")
  private String cin;

  @Pattern(regexp = "\\d{10}", message = "Telephone must be 10 digits")
  private String telephone;

  @NotBlank(message = "Operateur is required")
  private String operateur;

  @NotBlank(message = "EP Type is required")
  private String epType;

  @NotBlank(message = "Raison sociale is required")
  private String raisonSociale;

  @NotBlank(message = "Register number is required")
  private String registerNumber;

  @NotBlank(message = "Password is required")
  private String password;

  @NotBlank(message = "role is required")
  private String role;

}
