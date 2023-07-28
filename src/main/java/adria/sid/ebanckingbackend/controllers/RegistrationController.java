package adria.sid.ebanckingbackend.controllers;

import adria.sid.ebanckingbackend.dtos.ForgetPassword;
import adria.sid.ebanckingbackend.dtos.ReqRegisterClientMoraleDTO;
import adria.sid.ebanckingbackend.dtos.ReqRegisterClientPhysiqueDTO;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.security.emailToken.VerificationToken;
import adria.sid.ebanckingbackend.security.emailToken.VerificationTokenRepository;
import adria.sid.ebanckingbackend.services.authentification.AuthentificationService;
import adria.sid.ebanckingbackend.services.email.EmailSender;
import adria.sid.ebanckingbackend.utils.codeGenerators.HtmlCodeGenerator;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/register/client")
@CrossOrigin("*")
public class RegistrationController {

    private final AuthentificationService authenticationService;
    private final VerificationTokenRepository tokenRepository;
    private final HtmlCodeGenerator htmlCodeGenerator;
    private final EmailSender emailSender;

    @PostMapping("/physique")
    public String registerClientPhysique(@RequestBody ReqRegisterClientPhysiqueDTO reqRegisterClientPhysiqueDTO, final HttpServletRequest request) {
        authenticationService.registerClientPhysique(reqRegisterClientPhysiqueDTO, applicationUrl(request));
        return "Bravo ! Check your e-mail to finalize your inscription";
    }

    @PostMapping("/morale")
    public String registerClientMorale(@RequestBody ReqRegisterClientMoraleDTO reqRegisterClientMoraleDTO, final HttpServletRequest request) {
        authenticationService.registerClientMorale(reqRegisterClientMoraleDTO, applicationUrl(request));
        return "Bravo ! Check your e-mail to finalize your inscription";
    }

    @GetMapping("/verifieremail")
    public String verifyEmail(@RequestParam("token") String token) {
        VerificationToken theToken = tokenRepository.findByToken(token);
        if (theToken.getUser().getEnabled()) {
            return htmlCodeGenerator.generateVerifiedEmailHTML("This account has already been verified. Please log in.");
        }
        String verificationResult = authenticationService.validateToken(token);
        if (verificationResult.equalsIgnoreCase("valid")) {
            return htmlCodeGenerator.generateVerifiedEmailHTML("Email successfully verified. You can now log in.<br/> Your contracts are available for signature, this is the last step before finalizing the opening of your account.");
        }
        return htmlCodeGenerator.generateVerifiedEmailHTML("Invalid verification token.");
    }

    @GetMapping("/nouveaumdp")
    public String resetPassword(
            @RequestParam("token") String passwordResetToken,
            @RequestParam String password) {
        System.out.println("after all" + password);
        String tokenValidationResult = authenticationService.validatePasswordResetToken(passwordResetToken);
        if (!tokenValidationResult.equalsIgnoreCase("valid")) {
            return "Token invalid";
        }
        UserEntity user = authenticationService.findUserByPasswordToken(passwordResetToken);
        if (user != null) {
            authenticationService.resetUserPassword(user, password);
            return "Le mot de passe a été réinitialisé avec succès";
        }
        return "Token pour réinitialiser le mot de passe est invalide";
    }


    @PostMapping("/motdepasseoublie")
    public String resetPasswordRequest(@RequestBody ForgetPassword passwordResetRequest,
                                       final HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        Optional<UserEntity> user = authenticationService.findByEmail(passwordResetRequest.getEmail());

        String passwordResetUrl = "";
        if (user.isPresent()) {
            String passwordResetToken = UUID.randomUUID().toString();
            authenticationService.createPasswordResetTokenForUser(user.get(), passwordResetToken);
            System.out.println("confirmed password : "+passwordResetRequest.getPassword());
            passwordResetUrl = passwordResetEmailLink(user.get(), applicationUrl(request), passwordResetToken,passwordResetRequest.getPassword());
        }
        return passwordResetUrl;
    }

    private String passwordResetEmailLink(UserEntity user, String applicationUrl, String passwordResetToken,String pw) throws MessagingException, UnsupportedEncodingException {
        String url = applicationUrl + "/api/v1/register/client/nouveaumdp?token=" + passwordResetToken+"&password="+pw;
        emailSender.sentPasswordResetVerificationEmail(user,url);
        log.info("Cliquez ici pour renouvellez votre mot de passe : {}", url);
        return url;
    }

    public String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
