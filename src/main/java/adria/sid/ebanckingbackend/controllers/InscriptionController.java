package adria.sid.ebanckingbackend.controllers;

import adria.sid.ebanckingbackend.dtos.ReqRegisterClientDTO;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.security.emailToken.VerificationToken;
import adria.sid.ebanckingbackend.security.emailToken.VerificationTokenRepository;
import adria.sid.ebanckingbackend.sender.RegistrationEvent;
import adria.sid.ebanckingbackend.services.AuthenticationService;
import adria.sid.ebanckingbackend.utils.HtmlCodeGenerator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/inscription")
@CrossOrigin("*")
public class InscriptionController {

    private final AuthenticationService userService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenRepository tokenRepository;
    private final HtmlCodeGenerator htmlCodeGenerator;

    @PostMapping
    public String registerUser(@RequestBody ReqRegisterClientDTO registrationRequest, final HttpServletRequest request) {
        UserEntity userEntity = userService.registerClient(registrationRequest);
        publisher.publishEvent(new RegistrationEvent(userEntity, applicationUrl(request)));
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
