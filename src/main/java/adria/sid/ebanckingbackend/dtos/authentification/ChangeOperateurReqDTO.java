package adria.sid.ebanckingbackend.dtos.authentification;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeOperateurReqDTO {
    @NotNull
    String userId;
    @NotNull
    String operateur;
}
