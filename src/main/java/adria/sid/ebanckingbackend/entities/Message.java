package adria.sid.ebanckingbackend.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "compte_id")
public class Message {
    @Id
    private String id;

    private String message;

    private Boolean isRead;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private PJ pj;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banquier_id")
    private Banquier banquier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

}
