package adria.sid.ebanckingbackend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Banquier extends User{

    @OneToOne(mappedBy = "banquier")
    private Personne personne;

    @OneToMany(mappedBy = "banquier", cascade = CascadeType.ALL)
    private List<Message> messages;

    public void addMsg(Message msg) {
        if (messages == null) {
            messages = new ArrayList<>();
        }
        messages.add(msg);
        msg.setBanquier(this); // Set the user reference in the notification
    }

}
