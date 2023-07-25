package adria.sid.ebanckingbackend.sender.listener;

import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.sender.RegistrationEvent;
import adria.sid.ebanckingbackend.services.AuthenticationService;
import adria.sid.ebanckingbackend.utils.HtmlCodeGenerator;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendMail implements ApplicationListener<RegistrationEvent> {
 private final AuthenticationService userService;
 private final JavaMailSender mailSender;
 private final HtmlCodeGenerator htmlCodeGenerator;
 private UserEntity theUser;
    @Override
    public void onApplicationEvent(RegistrationEvent event) {
        theUser = event.getUser();
        String verificationToken = UUID.randomUUID().toString();
        userService.saveUserVerificationToken(theUser, verificationToken);
        String url = event.getApplicationUrl()+"/inscription/verifieremail?token="+verificationToken;
        try {
            sendVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        log.info("Cliquez sur le lien pour vérifier votre inscription :  {}", url);
    }

    public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "L'activation du compte utilisateur";
        String senderName = "L'activation du compte utilisateur";

        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("etafweb2021@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        System.out.println(theUser.toString());
        System.out.println(htmlCodeGenerator.generateActivatedEmailHTML(url,theUser));
        messageHelper.setText(htmlCodeGenerator.generateActivatedEmailHTML(url,theUser), true);
        mailSender.send(message);
    }
}
