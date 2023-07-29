package adria.sid.ebanckingbackend.services.authentification;

import adria.sid.ebanckingbackend.entities.UserEntity;

import java.util.Optional;

public interface MotdepasseTokenService {
    void createPasswordResetTokenForUser(UserEntity user, String passwordToken);
    String validatePasswordResetToken(String theToken);
    Optional<UserEntity> findUserByPasswordToken(String passwordToken);
}
