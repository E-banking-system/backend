package adria.sid.ebanckingbackend.sender.listener;

import adria.sid.ebanckingbackend.services.email.EmailCorps;
import adria.sid.ebanckingbackend.sender.SendeEmailEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendeEmail implements ApplicationListener<SendeEmailEvent> {
    private final JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(SendeEmailEvent event) {
        try {
            sendeEmail(event.getEmailCorps());
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendeEmail(EmailCorps emailCorps) throws MessagingException, UnsupportedEncodingException{
        String subject = emailCorps.getSubject();
        String senderName = emailCorps.getSenderName();

        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom(emailCorps.getFromEmail(), senderName);
        messageHelper.setTo(emailCorps.getToEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(emailCorps.getBody(), true);
        mailSender.send(message);
    }
}

