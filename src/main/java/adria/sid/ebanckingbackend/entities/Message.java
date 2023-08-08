package adria.sid.ebanckingbackend.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Message {
    @Id
    private String id;
    private String message;
    private Boolean isRead=false;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private PJ pj;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
