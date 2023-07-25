package adria.sid.ebanckingbackend.sender.listener;

import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.sender.RegistrationEvent;
import adria.sid.ebanckingbackend.services.AuthenticationService;
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
        String mailContent = "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<style>"
                + "/* Styles pour rendre l'email attrayant */"
                + "body {"
                + "    font-family: Arial, sans-serif;"
                + "    background-color: #f5f5f5;"
                + "    color: #333;"
                + "    margin: 0;"
                + "    padding: 0;"
                + "}"
                + ".container {"
                + "    max-width: 600px;"
                + "    margin: 0 auto;"
                + "    padding: 20px;"
                + "    background-color: #fff;"
                + "    border-radius: 8px;"
                + "    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);"
                + "}"
                + "h1 {"
                + "    color: #007bff;"
                + "}"
                + "p {"
                + "    margin-bottom: 15px;"
                + "}"
                + "a {"
                + "    display: inline-block;"
                + "    padding: 10px 20px;"
                + "    background-color: #007bff;"
                + "    color: #fff;"
                + "    text-decoration: none;"
                + "    border-radius: 5px;"
                + "    font-weight: bold;"
                + "}"
                +  ".text-link {"
                +  "    color: white;"
                +  "}"
                + "a:hover {"
                + "    background-color: #0056b3;"
                + "}"
                + "b {"
                + "    font-weight: bold;"
                + "}"
                + ".activate-link {"
                + "    color: #fff;"
                + "}"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class=\"container\">"
                + "<h1>Bienvenue, " + theUser.getNom() + " " + theUser.getPrenom() + "!</h1>"
                + "<p>Merci pour votre souscription, veuillez suivre les instructions pour finaliser votre inscription.</p>"
                + "<a class=\"activate-link\" href=\"" + url + "\"><div class=\"text-link\">Cliquez ici pour activer votre compte utilisateur</div></a>"
                + "<p>Merci <br> <b>BANQUE XXX</b></p>"
                + "</div>"
                + "</body>"
                + "</html>";

        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("etafweb2021@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }


}
