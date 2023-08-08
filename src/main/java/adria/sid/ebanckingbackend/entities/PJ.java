package adria.sid.ebanckingbackend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.File;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PJ {
    @Id
    private String id;
    private String name;
    private File file;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "message_id")
    private Message message;
}
