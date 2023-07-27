package adria.sid.ebanckingbackend.services;

import adria.sid.ebanckingbackend.entities.UserEntity;
import org.springframework.stereotype.Service;

@Service
public interface EmailSender {
    void sendVerificationUrlByEmail(UserEntity userEntity,String verificationToken,String applicationUrl);
    void sendAccountInfosByEmail(UserEntity userEntity, String pin);
}
