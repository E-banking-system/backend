package adria.sid.ebanckingbackend.dtos.virement;

import adria.sid.ebanckingbackend.ennumerations.EVType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VirementResDTO {
    private String id;
    private Date dateOperation;
    private Double montant;
}
