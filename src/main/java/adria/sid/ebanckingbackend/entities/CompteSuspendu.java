package adria.sid.ebanckingbackend.entities;

import jakarta.persistence.Entity;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CompteSuspendu extends Compte{
    private Date dateSuspention;
    private Boolean estSuspendu;
}
