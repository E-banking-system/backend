package adria.sid.ebanckingbackend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Client extends User{
    private String operateur;
    private String address;
    private Long tel;
    private String CIN;
    private String email;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Virement> virements;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Message> messages;

    public void addMsg(Message msg) {
        if (messages == null) {
            messages = new ArrayList<>();
        }
        messages.add(msg);
        msg.setClient(this); // Set the user reference in the notification
    }

    public void addVirement(Virement vrmnt) {
        if (virements == null) {
            virements = new ArrayList<>();
        }
        virements.add(vrmnt);
        vrmnt.setClient(this); // Set the user reference in the notification
    }

}
