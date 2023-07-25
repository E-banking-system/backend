package adria.sid.ebanckingbackend.controllers;

import adria.sid.ebanckingbackend.dtos.AuthResDTO;
import adria.sid.ebanckingbackend.dtos.ReqRegisterClientDTO;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.security.token.VerificationToken;
import adria.sid.ebanckingbackend.security.token.VerificationTokenRepository;
import adria.sid.ebanckingbackend.sender.RegistrationEvent;
import adria.sid.ebanckingbackend.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/inscription")
@CrossOrigin("*")
public class InscriptionController {

    private final AuthenticationService userService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenRepository tokenRepository;

    @PostMapping
    public String registerUser(@RequestBody ReqRegisterClientDTO registrationRequest, final HttpServletRequest request) {
        UserEntity userEntity = userService.registerClient(registrationRequest);
        publisher.publishEvent(new RegistrationEvent(userEntity, applicationUrl(request)));
        return "Bravo ! check your e-mail pour finaliser votre inscription";
    }

    @GetMapping("/verifieremail")
    public String verifyEmail(@RequestParam("token") String token) {
        VerificationToken theToken = tokenRepository.findByToken(token);
        if (theToken.getUser().isEnabled()) {
            return generateVerifiedEmailHTML("Ce compte a déjà été vérifié, merci de vous connecter.");
        }
        String verificationResult = userService.validateToken(token);
        if (verificationResult.equalsIgnoreCase("valid")) {
            return generateVerifiedEmailHTML("Email vérifié avec succès. Vous pouvez maintenant vous connecter.<br/> Vos contrats sont disponibles pour signature, c'est la dernière étape avant de finaliser l'ouverture de votre compte.");
        }
        return generateVerifiedEmailHTML("Token de vérification invalide.");
    }

    private String generateVerifiedEmailHTML(String message) {
        return "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                + "<title>Email Verification</title>"
                + "<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css\" rel=\"stylesheet\">"
                + "<style>"
                + "/* Custom Styles */"
                + "body {"
                + "    font-family: Arial, sans-serif;"
                + "    background-color: #f5f5f5;"
                + "    color: #333;"
                + "    margin: 0;"
                + "    padding: 0;"
                + "}"
                + ".container {"
                + "    max-width: 600px;"
                + "    margin: 50px auto;"
                + "    padding: 20px;"
                + "    background-color: #fff;"
                + "    border-radius: 8px;"
                + "    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);"
                + "}"
                + "h1 {"
                + "    color: #007bff;"
                + "    text-align: center;"
                + "}"
                + "p {"
                + "    margin-bottom: 15px;"
                + "}"
                + "a {"
                + "    display: inline-block;"
                + "    padding: 10px 20px;"
                + "    background-color: #007bff;"
                + "    color: #fff;"
                + "    text-decoration: none;"
                + "    border-radius: 5px;"
                + "    font-weight: bold;"
                + "}"
                + ".text-link {"
                + "    color: white;"
                + "}"
                + "a:hover {"
                + "    background-color: #0056b3;"
                + "}"
                + "b {"
                + "    font-weight: bold;"
                + "}"
                + ".activate-link {"
                + "    color: #fff;"
                + "}"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class=\"container\">"
                + "<h1>Welcome</h1>"
                + "<p>" + message + "</p>"
                + "<p>Merci<br><b>BANK XXX</b></p>"
                + "</div>"
                + "</body>"
                + "</html>";
    }


    public String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
