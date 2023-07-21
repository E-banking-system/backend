package adria.sid.ebanckingbackend.entities;

import adria.sid.ebanckingbackend.ennumerations.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "role_id")
public class Role {

    @Id
    private String id;

    private ERole name;

    /*@OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
    private List<User> users;*/

}
