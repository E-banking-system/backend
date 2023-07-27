package adria.sid.ebanckingbackend.sender;

import adria.sid.ebanckingbackend.entities.EmailCorps;
import adria.sid.ebanckingbackend.entities.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;


@Getter
@Setter
public class SendeEmailEvent extends ApplicationEvent {
    private UserEntity user;
    private EmailCorps emailCorps;

    public SendeEmailEvent(UserEntity user, EmailCorps emailCorps) {
        super(user);
        this.user = user;
        this.emailCorps = emailCorps;
    }
}
