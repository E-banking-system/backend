package adria.sid.ebanckingbackend.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Banquier extends Personne{

    @OneToOne(mappedBy = "banquier")
    private Personne personne;

    @OneToMany(mappedBy = "banquier", cascade = CascadeType.ALL)
    private List<Message> messages;
}
