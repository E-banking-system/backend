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
        public void sentPasswordResetVerificationEmail(UserEntity userEntity, String url) {
            String subject = "Réinsialiser votre mot de passe";
            String senderName = "For you";
            String mailContent = "<p> Hey, "+ userEntity.getNom()+ ", </p>"+
                    "<p>Vous avez demandé à reinsialiser votre mot de passe,"+"" +
                    "Merci de suivre les instructions.</p>"+
                    "<a href=\"" +url+ "\">Réinsialiser le mot de passe</a>"+
                    "<p> For you project";

            EmailCorps emailCorps = new EmailCorps();
            emailCorps.setFromEmail("etafweb2021@gmail.com");
            emailCorps.setToEmail(userEntity.getEmail());
            emailCorps.setBody(mailContent);
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
