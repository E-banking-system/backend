package adria.sid.ebanckingbackend.controllers;

import adria.sid.ebanckingbackend.dtos.ReqRegisterClientMoraleDTO;
import adria.sid.ebanckingbackend.ennumerations.EtatCompte;
import adria.sid.ebanckingbackend.entities.Compte;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.repositories.UserRepository;
import adria.sid.ebanckingbackend.security.emailToken.VerificationTokenRepository;
import adria.sid.ebanckingbackend.sender.RegistrationEvent;
import adria.sid.ebanckingbackend.services.AuthenticationService;
import adria.sid.ebanckingbackend.utils.HtmlCodeGenerator;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/banquier")
@Tag(name = "Banquier")
@CrossOrigin("*")
@RequiredArgsConstructor
public class BanquierController {

    private final AuthenticationService userService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenRepository tokenRepository;
    private final HtmlCodeGenerator htmlCodeGenerator;
    private final UserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasAuthority('banquier:read')")
    public String get() {
        return "GET:: banquier controller";
    }
    @PostMapping
    @PreAuthorize("hasAuthority('banquier:create')")
    @Hidden
    public String post() {
        return "POST:: banquier controller";
    }
    @PutMapping
    @PreAuthorize("hasAuthority('banquier:update')")
    @Hidden
    public String put() {
        return "PUT:: banquier controller";
    }
    @DeleteMapping
    @PreAuthorize("hasAuthority('banquier:delete')")
    @Hidden
    public String delete() {
        return "DELETE:: banquier controller";
    }

    @PostMapping("/suiteRegistrationClient")
    @PreAuthorize("hasAuthority('banquier:create')")
    public ResponseEntity<String> registerClientMorale(@RequestParam("email") String email, final HttpServletRequest request) {
        // Check if the user with the same email already exists
        Optional<UserEntity> existingUserOptional = userRepository.findByEmail(email);

        if (existingUserOptional.isPresent()) {
            // User with the same email already exists, call the createAccountForExistingUser endpoint
            ResponseEntity<String> createAccountResponse = createAccountForExistingUser(email, request);
            return createAccountResponse;
        }

        // User does not exist, register a new client
        //UserEntity userEntity = userService.registerClientMorale(registrationRequest);

        // Send the registration event (optional)
        //publisher.publishEvent(new RegistrationEvent(userEntity, applicationUrl(request)));

        return ResponseEntity.ok("client n'existe pas");
    }

    @PostMapping("/createAccount")
    @PreAuthorize("hasAuthority('banquier:create')")
    public ResponseEntity<String> createAccountForExistingUser(@RequestParam("email") String email, final HttpServletRequest request) {
        // Find the existing user with the provided email
        Optional<UserEntity> existingUserOptional = userRepository.findByEmail(email);

        if (existingUserOptional.isPresent()) {
            // User with the same email already exists, create an account for them
            UserEntity existingUser = existingUserOptional.get();
            Compte newCompte = new Compte();
            newCompte.setId(UUID.randomUUID().toString());
            newCompte.setNature("Nature"); // Set the nature as per your requirement
            newCompte.setSolde(0.0); // Set the initial solde
            newCompte.setRIB("RIB"); // Set the RIB as per your requirement
            newCompte.setNumCompte(189299336L); // Set the account number as per your requirement
            newCompte.setDateCreation(new Date());
            newCompte.setDatePeremption(new Date());
            newCompte.setEtatCompte(EtatCompte.ACTIVE); // Set the initial state of the account

            // Add the new account to the existing user's list of accounts
            existingUser.addCompte(newCompte);

            // Save the changes to the existing user (this will also save the new account)
            userRepository.save(existingUser);

            // Send the registration event (optional)
            publisher.publishEvent(new RegistrationEvent(existingUser, applicationUrl(request)));

            return ResponseEntity.ok("Bravo ! Un compte a été créé pour cet utilisateur. Check your e-mail pour finaliser votre inscription");
        }

        return ResponseEntity.notFound().build();
    }



    public String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
