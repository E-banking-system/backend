package adria.sid.ebanckingbackend.dtos.message;

import adria.sid.ebanckingbackend.ennumerations.MessageType;
import adria.sid.ebanckingbackend.entities.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageFileResDTO {
    private String id;
    private Boolean isReaded;
    private MessageType type;
    private String content;
    private Date localDateTime;
    private byte[] fileData;
    private String fileName;
    private String fileType;
}
