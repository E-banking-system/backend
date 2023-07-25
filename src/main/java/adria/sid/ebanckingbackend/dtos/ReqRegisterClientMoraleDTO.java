package adria.sid.ebanckingbackend.dtos;

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
public class ReqRegisterClientMoraleDTO {

  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format")
  private String email;

  @NotBlank(message = "Adresse is required")
  private String adresse;

  @Pattern(regexp = "\\d{10}", message = "Telephone must be 10 digits")
  private String telephone;

  @NotBlank(message = "Operateur is required")
  private String operateur;

  @NotBlank(message = "Raison sociale is required")
  private String raisonSociale;

  @NotBlank(message = "Register number is required")
  private String registerNumber;

  @NotBlank(message = "Password is required")
  private String password;

}
