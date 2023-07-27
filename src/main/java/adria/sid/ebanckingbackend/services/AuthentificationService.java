package adria.sid.ebanckingbackend.services;

import adria.sid.ebanckingbackend.dtos.AuthReqDTO;
import adria.sid.ebanckingbackend.dtos.AuthResDTO;
import adria.sid.ebanckingbackend.dtos.ReqRegisterClientMoraleDTO;
import adria.sid.ebanckingbackend.dtos.ReqRegisterClientPhysiqueDTO;
import adria.sid.ebanckingbackend.entities.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


public interface AuthentificationService {
    void saveUserVerificationToken(UserEntity theUser, String token);
    String validateToken(String theToken);
    UserEntity registerClientPhysique(ReqRegisterClientPhysiqueDTO request, String url);
    UserEntity registerClientMorale(ReqRegisterClientMoraleDTO request, String url);
    AuthResDTO authenticate(AuthReqDTO request);
    void saveUserToken(UserEntity user, String jwtToken);
    void revokeAllUserTokens(UserEntity user);
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
