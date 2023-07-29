package adria.sid.ebanckingbackend.services.authentification;

import adria.sid.ebanckingbackend.dtos.AuthReqDTO;
import adria.sid.ebanckingbackend.dtos.AuthResDTO;
import adria.sid.ebanckingbackend.dtos.ClientMoraleDTO;
import adria.sid.ebanckingbackend.dtos.ClientPhysiqueDTO;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.exceptions.UserHasNotAnyCompte;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;


public interface AuthentificationService {
    void saveUserVerificationToken(UserEntity theUser, String token);
    String validateToken(String theToken);
    UserEntity registerClientPhysique(ClientPhysiqueDTO clientPhysiqueDTO, String url);
    UserEntity registerClientMorale(ClientMoraleDTO clientMoraleDTO, String url);
    AuthResDTO authenticate(AuthReqDTO request) throws UserHasNotAnyCompte;
    void saveUserToken(UserEntity user, String jwtToken);
    void revokeAllUserTokens(UserEntity user);
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
    void createPasswordResetTokenForUser(UserEntity user, String passwordToken);
    String validatePasswordResetToken(String passwordResetToken);
    UserEntity findUserByPasswordToken(String passwordResetToken);
    void resetUserPassword(UserEntity user, String newPassword);
    Optional<UserEntity> findByEmail(String email);
    void sendPasswordResetEmail(UserEntity user, String resetPasswordUrl);
}
