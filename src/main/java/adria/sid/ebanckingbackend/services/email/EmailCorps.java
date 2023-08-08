package adria.sid.ebanckingbackend.services.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailCorps {
    private String subject;
    private String senderName;
    private String fromEmail;
    private String toEmail;
    private String body;
}
