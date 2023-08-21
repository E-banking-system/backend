package adria.sid.ebanckingbackend.services.authentification;

import adria.sid.ebanckingbackend.dtos.authentification.AuthReqDTO;
import adria.sid.ebanckingbackend.dtos.authentification.AuthResDTO;
import adria.sid.ebanckingbackend.dtos.authentification.ChangeOperateurReqDTO;
import adria.sid.ebanckingbackend.dtos.authentification.UserInfosResDTO;
import adria.sid.ebanckingbackend.dtos.client.ClientMoraleDTO;
import adria.sid.ebanckingbackend.dtos.client.ClientPhysiqueDTO;
import adria.sid.ebanckingbackend.dtos.client.ClientResDTO;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.exceptions.UserHasNotAnyCompte;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.Optional;


public interface AuthenticationService {
    Boolean changeOperateur(ChangeOperateurReqDTO changeOperateurReqDTO);
    UserInfosResDTO getUserInfos(String userId);
    Page<ClientResDTO> getClientVirements(Pageable pageable);
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
    Page<ClientResDTO> searchClients(Pageable pageable, String keyword);
}
