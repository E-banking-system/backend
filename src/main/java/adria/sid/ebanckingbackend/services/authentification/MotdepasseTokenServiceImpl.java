package adria.sid.ebanckingbackend.services.authentification;

import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.security.passwordToken.MotdePasseTokenRepository;
import adria.sid.ebanckingbackend.security.passwordToken.MotdepasseToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MotdepasseTokenServiceImpl implements MotdepasseTokenService{

    private final MotdePasseTokenRepository passwordResetTokenRepository;

    @Override
    public void createPasswordResetTokenForUser(UserEntity user, String passwordToken) {
        MotdepasseToken passwordResetToken = new MotdepasseToken(passwordToken, user);
        passwordResetTokenRepository.save(passwordResetToken);

        log.info("Created password reset token for user: {}", user.getUsername());
    }

    @Override
    public String validatePasswordResetToken(String theToken) {
        MotdepasseToken token = passwordResetTokenRepository.findByToken(theToken);
        if (token == null) {
            log.warn("Invalid password reset token: {}", theToken);
            return "Token pour réinitialiser le mot de passe invalide";
        }

        UserEntity user = token.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
            log.warn("Password reset token has expired for user: {}", user.getUsername());
            return "Le lien a expiré, envoyez un nouveau mail";
        }

        log.info("Valid password reset token for user: {}", user.getUsername());
        return "valid";
    }

    @Override
    public Optional<UserEntity> findUserByPasswordToken(String passwordToken) {
        MotdepasseToken token = passwordResetTokenRepository.findByToken(passwordToken);
        UserEntity user = (token != null) ? token.getUser() : null;
        if (user != null) {
            log.info("Found user with password reset token: {}", user.getUsername());
        } else {
            log.info("No user found with password reset token: {}", passwordToken);
        }
        return Optional.ofNullable(user);
    }
}