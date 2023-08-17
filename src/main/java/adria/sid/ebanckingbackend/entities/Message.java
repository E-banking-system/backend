package adria.sid.ebanckingbackend.entities;

import adria.sid.ebanckingbackend.ennumerations.MessageType;
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
    private String message;
    private Boolean isRead=false;
    private MessageType type;
    private String content;
    private Date localDateTime;

    private LocalDateTime timestamp; // Timestamp of the message

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")*/
    private String sender;

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")*/
    private String receiver;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private PJ pj;

}
