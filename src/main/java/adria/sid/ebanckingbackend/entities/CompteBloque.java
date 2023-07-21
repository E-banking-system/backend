package adria.sid.ebanckingbackend.entities;

import jakarta.persistence.Entity;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class CompteBloque extends Compte{
    private Date dateBloquage;
    private Boolean estBloque;
}
