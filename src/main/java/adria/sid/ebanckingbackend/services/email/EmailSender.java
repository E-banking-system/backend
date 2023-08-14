package adria.sid.ebanckingbackend.services.email;

import adria.sid.ebanckingbackend.entities.UserEntity;
import org.springframework.stereotype.Service;

@Service
public interface EmailSender {
    void sendVerificationUrlByEmail(UserEntity userEntity,String verificationToken,String applicationUrl);
    void sentPasswordResetVerificationEmail(UserEntity userEntity,String url);
    void sendAccountInfosByEmail(UserEntity userEntity, String pin);
    void sendUnitTransferVerificationByEmail(UserEntity userEntity,String otpCode);
}
