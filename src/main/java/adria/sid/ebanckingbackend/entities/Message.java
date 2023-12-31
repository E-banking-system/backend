package adria.sid.ebanckingbackend.entities;

import adria.sid.ebanckingbackend.ennumerations.MessageType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Message {
    @Id
    private String id;
    private Boolean isReaded=false;
    private MessageType type;
    private String content;
    private Date localDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private UserEntity sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private UserEntity receiver;

    // some fields for sending files
    private byte[] fileData;
    private String fileName;
    private String fileType;

}
