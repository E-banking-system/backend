package adria.sid.ebanckingbackend.dtos.compte;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlockerCompteReqDTO {
    @NotNull(message = "id is required")
    private String compteId;
}
