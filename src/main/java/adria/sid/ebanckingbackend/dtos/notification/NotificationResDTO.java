package adria.sid.ebanckingbackend.dtos.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResDTO {
    private String id;
    private String titre;
    private String contenu;
    private Date dateEnvoie;
}
