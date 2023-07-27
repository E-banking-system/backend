package adria.sid.ebanckingbackend.controllers;

import adria.sid.ebanckingbackend.dtos.ReqRegisterClientMoraleDTO;
import adria.sid.ebanckingbackend.dtos.ReqRegisterClientPhysiqueDTO;
import adria.sid.ebanckingbackend.entities.EmailCorps;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.security.emailToken.VerificationToken;
import adria.sid.ebanckingbackend.security.emailToken.VerificationTokenRepository;
import adria.sid.ebanckingbackend.sender.SendeEmailEvent;
import adria.sid.ebanckingbackend.services.AuthenticationService;
import adria.sid.ebanckingbackend.services.EmailSender;
import adria.sid.ebanckingbackend.utils.HtmlCodeGenerator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/register/client")
@CrossOrigin("*")
public class RegistrationController {

    private final AuthenticationService userService;
    private final VerificationTokenRepository tokenRepository;
    private final HtmlCodeGenerator htmlCodeGenerator;

    @PostMapping("/physique")
    public String registerClientPhysique(@RequestBody ReqRegisterClientPhysiqueDTO registrationRequest, final HttpServletRequest request) {
        userService.registerClientPhysique(registrationRequest,applicationUrl(request));
        return "Bravo ! check your e-mail pour finaliser votre inscription";
    }

    @PostMapping("/morale")
    public String registerClientMorale(@RequestBody ReqRegisterClientMoraleDTO registrationRequest, final HttpServletRequest request) {
        userService.registerClientMorale(registrationRequest,applicationUrl(request));
        return "Bravo ! check your e-mail pour finaliser votre inscription";
    }

    @GetMapping("/verifieremail")
    public String verifyEmail(@RequestParam("token") String token) {
        VerificationToken theToken = tokenRepository.findByToken(token);
        if (theToken.getUser().getEnabled()) {
            return htmlCodeGenerator.generateVerifiedEmailHTML("Ce compte a déjà été vérifié, merci de vous connecter.");
        }
        String verificationResult = userService.validateToken(token);
        if (verificationResult.equalsIgnoreCase("valid")) {
            return htmlCodeGenerator.generateVerifiedEmailHTML("Email vérifié avec succès. Vous pouvez maintenant vous connecter.<br/> Vos contrats sont disponibles pour signature, c'est la dernière étape avant de finaliser l'ouverture de votre compte.");
        }
        return htmlCodeGenerator.generateVerifiedEmailHTML("Token de vérification invalide.");
    }

    public String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
