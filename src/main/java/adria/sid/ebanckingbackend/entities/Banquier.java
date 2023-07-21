package adria.sid.ebanckingbackend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "banquier_id")
public class Banquier extends User{

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "personne_id")
    private Personne personne;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "banquier")
    private List<Message> messages;

    public void addMsg(Message msg) {
        if (messages == null) {
            messages = new ArrayList<>();
        }
        messages.add(msg);
        msg.setBanquier(this); // Set the user reference in the notification
    }

}
