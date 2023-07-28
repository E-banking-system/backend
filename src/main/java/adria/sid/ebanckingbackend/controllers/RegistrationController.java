package adria.sid.ebanckingbackend.controllers;

import adria.sid.ebanckingbackend.dtos.ReqRegisterClientMoraleDTO;
import adria.sid.ebanckingbackend.dtos.ReqRegisterClientPhysiqueDTO;
import adria.sid.ebanckingbackend.security.emailToken.VerificationToken;
import adria.sid.ebanckingbackend.security.emailToken.VerificationTokenRepository;
import adria.sid.ebanckingbackend.services.authentification.AuthentificationService;
import adria.sid.ebanckingbackend.utils.codeGenerators.HtmlCodeGenerator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/register/client")
@CrossOrigin("*")
public class RegistrationController {

    private final AuthentificationService authenticationService;
    private final VerificationTokenRepository tokenRepository;
    private final HtmlCodeGenerator htmlCodeGenerator;

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

    public String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
