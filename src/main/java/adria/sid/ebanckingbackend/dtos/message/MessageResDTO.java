package adria.sid.ebanckingbackend.dtos.message;

import adria.sid.ebanckingbackend.ennumerations.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResDTO {
    private String id;
    private Boolean isReaded;
    private MessageType type;
    private String content;
    private Date localDateTime;
    private String sender;
    private String receiver;
}
