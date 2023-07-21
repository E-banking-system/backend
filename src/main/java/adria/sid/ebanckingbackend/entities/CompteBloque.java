package adria.sid.ebanckingbackend.entities;

import jakarta.persistence.Entity;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CompteBloque extends Compte{
    private Date dateBloquage;
    private Boolean estBloque;
}
