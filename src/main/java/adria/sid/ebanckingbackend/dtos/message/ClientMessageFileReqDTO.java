package adria.sid.ebanckingbackend.dtos.message;

import adria.sid.ebanckingbackend.ennumerations.MessageType;
import adria.sid.ebanckingbackend.entities.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientMessageFileReqDTO {
    private String id;
    private Boolean isReaded;
    private MessageType type;
    private String content;
    private Date localDateTime;
    private UserEntity sender;
    private UserEntity receiver;
    @JsonIgnore
    private byte[] fileData;
    private String fileName;
    private String fileType;
}
