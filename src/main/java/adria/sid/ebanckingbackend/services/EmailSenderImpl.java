package adria.sid.ebanckingbackend.services;

import adria.sid.ebanckingbackend.entities.EmailCorps;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.sender.SendeEmailEvent;
import adria.sid.ebanckingbackend.utils.HtmlCodeGenerator;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailSenderImpl implements EmailSender{
    private final ApplicationEventPublisher publisher;
    private final HtmlCodeGenerator htmlCodeGenerator;

    @Override
    public void sendVerificationUrlByEmail(UserEntity userEntity,String verificationToken,String applicationUrl) {
        EmailCorps emailCorps=new EmailCorps();
        String url = applicationUrl+"/api/v1/register/client/verifieremail?token="+verificationToken;
        String emailBody=htmlCodeGenerator.generateActivatedEmailHTML(url,userEntity);
        emailCorps.setFromEmail("etafweb2021@gmail.com");
        emailCorps.setToEmail(userEntity.getEmail());
        emailCorps.setBody(emailBody);
        emailCorps.setSenderName(userEntity.getNom());
        emailCorps.setSubject(userEntity.getPrenom());
        publisher.publishEvent(new SendeEmailEvent(userEntity,emailCorps));
    }

    @Override
    public void sendAccountInfosByEmail(UserEntity userEntity,String pin) {
        EmailCorps emailCorps=new EmailCorps();
        emailCorps.setBody(htmlCodeGenerator.generateActivatedAccountInfoEmail(pin,userEntity));
        emailCorps.setFromEmail("etafweb2021@gmail.com");
        emailCorps.setToEmail(userEntity.getEmail());
        emailCorps.setSenderName(userEntity.getNom());
        emailCorps.setSubject(userEntity.getPrenom());
        publisher.publishEvent(new SendeEmailEvent(userEntity,emailCorps));
    }
}
