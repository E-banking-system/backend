package adria.sid.ebanckingbackend.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String message;

    private Boolean isRead;

    @OneToOne(mappedBy = "message")
    private PJ pj;

    @ManyToOne
    @JoinColumn(name = "banquier_id")
    private Banquier banquier;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

}
