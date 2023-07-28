package adria.sid.ebanckingbackend.services.email;

import adria.sid.ebanckingbackend.entities.EmailCorps;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.sender.SendeEmailEvent;
import adria.sid.ebanckingbackend.utils.codeGenerators.HtmlCodeGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class EmailSenderImpl implements EmailSender {
    private final ApplicationEventPublisher publisher;
    private final HtmlCodeGenerator htmlCodeGenerator;

    @Override
    public void sendVerificationUrlByEmail(UserEntity userEntity, String verificationToken, String applicationUrl) {
        EmailCorps emailCorps = new EmailCorps();
        String url = applicationUrl + "/api/v1/register/client/verifieremail?token=" + verificationToken;
        String emailBody = htmlCodeGenerator.generateActivatedEmailHTML(url, userEntity);
        emailCorps.setFromEmail("etafweb2021@gmail.com");
        emailCorps.setToEmail(userEntity.getEmail());
        emailCorps.setBody(emailBody);
        emailCorps.setSenderName(userEntity.getNom());
        emailCorps.setSubject(userEntity.getPrenom());

        try {
            publisher.publishEvent(new SendeEmailEvent(userEntity, emailCorps));
            log.info("Verification email sent successfully to: {}", userEntity.getEmail());
        } catch (Exception e) {
            log.error("Failed to send verification email to: {}", userEntity.getEmail(), e);
        }
    }

    @Override
    public void sendAccountInfosByEmail(UserEntity userEntity, String pin) {
        EmailCorps emailCorps = new EmailCorps();
        emailCorps.setBody(htmlCodeGenerator.generateActivatedAccountInfoEmail(pin, userEntity));
        emailCorps.setFromEmail("etafweb2021@gmail.com");
        emailCorps.setToEmail(userEntity.getEmail());
        emailCorps.setSenderName(userEntity.getNom());
        emailCorps.setSubject(userEntity.getPrenom());

        try {
            publisher.publishEvent(new SendeEmailEvent(userEntity, emailCorps));
            log.info("Account info email sent successfully to: {}", userEntity.getEmail());
        } catch (Exception e) {
            log.error("Failed to send account info email to: {}", userEntity.getEmail(), e);
        }
    }
}
