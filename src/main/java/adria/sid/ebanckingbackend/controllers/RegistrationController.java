package adria.sid.ebanckingbackend.controllers;

import adria.sid.ebanckingbackend.dtos.password.ForgetPasswordDTO;
import adria.sid.ebanckingbackend.dtos.client.ClientMoraleDTO;
import adria.sid.ebanckingbackend.dtos.client.ClientPhysiqueDTO;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.exceptions.UserAlreadyExists;
import adria.sid.ebanckingbackend.security.emailToken.VerificationToken;
import adria.sid.ebanckingbackend.security.emailToken.VerificationTokenRepository;
import adria.sid.ebanckingbackend.services.authentification.AuthenticationService;
import adria.sid.ebanckingbackend.utils.codeGenerators.HtmlCodeGenerator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/register/client")
@CrossOrigin("*")
public class RegistrationController {

    private final AuthenticationService authenticationService;
    private final VerificationTokenRepository tokenRepository;
    private final HtmlCodeGenerator htmlCodeGenerator;

    @PostMapping("/physique")
    public String registerClientPhysique(@RequestBody @Valid ClientPhysiqueDTO reqRegisterClientPhysiqueDTO, final HttpServletRequest request) {
        try {
            authenticationService.registerClientPhysique(reqRegisterClientPhysiqueDTO, applicationUrl(request));
            return "Bravo ! Check your e-mail to finalize your inscription";
        } catch (UserAlreadyExists ex) {
            return "Error: " + ex.getMessage();
        } catch (Exception e) {
            return "An error occurred during registration: " + e.getMessage();
        }
    }

    @PostMapping("/morale")
    public String registerClientMorale(@RequestBody @Valid ClientMoraleDTO reqRegisterClientMoraleDTO, final HttpServletRequest request) {
        try {
            authenticationService.registerClientMorale(reqRegisterClientMoraleDTO, applicationUrl(request));
            return "Bravo ! Check your e-mail to finalize your inscription";
        } catch (UserAlreadyExists ex) {
            return "Error: " + ex.getMessage();
        } catch (Exception e) {
            return "An error occurred during registration: " + e.getMessage();
        }
    }

    @GetMapping("/verifieremail")
    public String verifyEmail(@RequestParam("token") String token) {
        try {
            VerificationToken theToken = tokenRepository.findByToken(token);
            if (theToken.getUser().getEnabled()) {
                return htmlCodeGenerator.generateVerifiedEmailHTML("This account has already been verified. Please log in.");
            }
            String verificationResult = authenticationService.validateToken(token);
            if (verificationResult.equalsIgnoreCase("valid")) {
                return htmlCodeGenerator.generateVerifiedEmailHTML("Email successfully verified. You can now log in.<br/> Your contracts are available for signature, this is the last step before finalizing the opening of your account.");
            }
            return htmlCodeGenerator.generateVerifiedEmailHTML("Invalid verification token.");
        } catch (Exception e) {
            return "An error occurred while verifying email: " + e.getMessage();
        }
    }

    @GetMapping("/nouveaumdp")
    public String resetPassword(
            @RequestParam("token") String passwordResetToken,
            @RequestParam String password) {
        try {
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
        } catch (Exception e) {
            return "An error occurred while resetting password: " + e.getMessage();
        }
    }


    @PostMapping("/motdepasseoublie")
    public String resetPasswordRequest(@RequestBody @Valid ForgetPasswordDTO passwordResetRequest, final HttpServletRequest request) {
        try {
            Optional<UserEntity> user = authenticationService.findByEmail(passwordResetRequest.getEmail());

            String passwordResetUrl = "";
            if (user.isPresent()) {
                String passwordResetToken = UUID.randomUUID().toString();
                authenticationService.createPasswordResetTokenForUser(user.get(), passwordResetToken);
                passwordResetUrl = passwordResetEmailLink(applicationUrl(request), passwordResetToken, passwordResetRequest.getPassword());
                authenticationService.sendPasswordResetEmail(user.get(), passwordResetUrl);
            } else {
                return "User not found"; // Return an appropriate error message if the user is not found.
            }

            return passwordResetUrl;
        } catch (Exception e) {
            return "An error occurred while processing password reset request: " + e.getMessage();
        }
    }

    private String passwordResetEmailLink(String applicationUrl, String passwordResetToken, String pw) {
        String url = applicationUrl + "/api/v1/register/client/nouveaumdp?token=" + passwordResetToken + "&password=" + pw;
        return url;
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}