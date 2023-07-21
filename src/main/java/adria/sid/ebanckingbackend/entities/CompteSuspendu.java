package adria.sid.ebanckingbackend.entities;

import jakarta.persistence.Entity;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class CompteSuspendu extends Compte{
    private Date dateSuspention;
    private Boolean estSuspendu;
}
