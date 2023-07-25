package adria.sid.ebanckingbackend.sender;

import adria.sid.ebanckingbackend.entities.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;


@Getter
@Setter
public class RegistrationEvent extends ApplicationEvent {
    private UserEntity user;
    private String applicationUrl;

    public RegistrationEvent(UserEntity user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }
}
