package adria.sid.ebanckingbackend.dtos.operation;

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
