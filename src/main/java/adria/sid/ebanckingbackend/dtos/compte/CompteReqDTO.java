package adria.sid.ebanckingbackend.dtos.compte;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompteReqDTO {
    @NotNull
    @NotBlank(message = "nature is required")
    private String nature;

    @NotNull(message = "solde is required")
    @Min(value = 0, message = "solde must be greater than zero")
    private Double solde;

    @NotNull
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
}
