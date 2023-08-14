package adria.sid.ebanckingbackend.services.email;

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
        String senderName;
        if(userEntity.getNom() != null){
            senderName = userEntity.getNom()+" "+userEntity.getPrenom();
        }
        else{
            senderName = userEntity.getRaisonSociale();
        }
        String url = applicationUrl + "/api/v1/register/client/verifieremail?token=" + verificationToken;
        String emailBody = htmlCodeGenerator.generateActivatedEmailHTML(url, senderName);
        emailCorps.setFromEmail("etafweb2021@gmail.com");
        emailCorps.setToEmail(userEntity.getEmail());
        emailCorps.setBody(emailBody);

        emailCorps.setSenderName(senderName);
        emailCorps.setSubject("vérification email");

        try {
            publisher.publishEvent(new SendeEmailEvent(userEntity, emailCorps));
            log.info("Verification email sent successfully to: {}", userEntity.getEmail());
        } catch (Exception e) {
            log.error("Failed to send verification email to: {}", userEntity.getEmail(), e);
        }
    }

    @Override
    public void sentPasswordResetVerificationEmail(UserEntity userEntity, String url) {
        String subject = "réinitialiser votre mot de passe";
        String senderName;
        if(userEntity.getNom() != null){
            senderName = userEntity.getNom()+" "+userEntity.getPrenom();
        }
        else{
            senderName = userEntity.getRaisonSociale();
        }
        EmailCorps emailCorps = new EmailCorps();
        emailCorps.setFromEmail("etafweb2021@gmail.com");
        emailCorps.setToEmail(userEntity.getEmail());
        emailCorps.setBody(htmlCodeGenerator.generateResetPasswordEmailHTML(senderName,url));
        emailCorps.setSenderName(senderName);
        emailCorps.setSubject(subject);

        try {
            publisher.publishEvent(new SendeEmailEvent(userEntity, emailCorps));
            log.info("Verification password by email sent successfully to: {}", userEntity.getEmail());
        } catch (Exception e) {
            log.error("Failed to send verification your password by email to: {}", userEntity.getEmail(), e);
        }
    }

    @Override
    public void sendAccountInfosByEmail(UserEntity userEntity, String pin) {
        EmailCorps emailCorps = new EmailCorps();
        String senderName;
        if(userEntity.getNom() != null){
            senderName = userEntity.getNom()+" "+userEntity.getPrenom();
        }
        else{
            senderName = userEntity.getRaisonSociale();
        }

        emailCorps.setBody(htmlCodeGenerator.generateActivatedAccountInfoEmail(pin, senderName));
        emailCorps.setFromEmail("etafweb2021@gmail.com");
        emailCorps.setToEmail(userEntity.getEmail());

        emailCorps.setSenderName(senderName);
        emailCorps.setSubject("account infos");

        try {
            publisher.publishEvent(new SendeEmailEvent(userEntity, emailCorps));
            log.info("Account info email sent successfully to: {}", userEntity.getEmail());
        } catch (Exception e) {
            log.error("Failed to send account info email to: {}", userEntity.getEmail(), e);
        }
    }

    @Override
    public void sendUnitTransferVerificationByEmail(UserEntity userEntity, String otpCode) {
        EmailCorps emailCorps = new EmailCorps();
        String senderName;
        if(userEntity.getNom() != null){
            senderName = userEntity.getNom()+" "+userEntity.getPrenom();
        }
        else{
            senderName = userEntity.getRaisonSociale();
        }
        emailCorps.setBody(htmlCodeGenerator.generateOTPverificationHTML(otpCode, senderName));
        emailCorps.setFromEmail("etafweb2021@gmail.com");
        emailCorps.setToEmail(userEntity.getEmail());

        emailCorps.setSenderName(senderName);
        emailCorps.setSubject(otpCode + " est votre code de vérification pour effectuer un virement unitaire");

        try {
            publisher.publishEvent(new SendeEmailEvent(userEntity, emailCorps));
            log.info("Account info email sent successfully to: {}", userEntity.getEmail());
        } catch (Exception e) {
            log.error("Failed to send account info email to: {}", userEntity.getEmail(), e);
        }
    }

}
