package adria.sid.ebanckingbackend.entities;

import adria.sid.ebanckingbackend.ennumerations.ERole;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private ERole name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
    private List<User> users;

    public void addUser(User user){
        if(users == null){
            users = new ArrayList<>();
        }
        users.add(user);
        user.setRole(this);
    }
}
