package adria.sid.ebanckingbackend.dtos.compte;

import adria.sid.ebanckingbackend.ennumerations.EtatCompte;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActiverCompteReqDTO {
    private String id;
    private EtatCompte etatCompte;
}
