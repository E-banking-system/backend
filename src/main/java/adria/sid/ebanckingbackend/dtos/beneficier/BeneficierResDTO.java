package adria.sid.ebanckingbackend.dtos.beneficier;

import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.entities.Virement;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BeneficierResDTO {
    private String beneficier_id;
    private String numCompte;
    private String user_manager_id;
    private String parent_user_id;
    private String nom;
    private String prenom;
}
