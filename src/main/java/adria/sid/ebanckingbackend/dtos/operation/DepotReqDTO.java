package adria.sid.ebanckingbackend.dtos.operation;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepotReqDTO {
    @NotNull
    private Double montant;
    @NotNull
    private String numCompte;
}
