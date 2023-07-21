package adria.sid.ebanckingbackend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Banquier extends Personne{

    @OneToOne(mappedBy = "banquier")
    private Personne personne;

    @OneToMany(mappedBy = "banquier", cascade = CascadeType.ALL)
    private List<Message> messages;
}
