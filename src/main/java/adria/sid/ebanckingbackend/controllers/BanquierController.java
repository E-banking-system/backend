package adria.sid.ebanckingbackend.controllers;

import adria.sid.ebanckingbackend.dtos.ReqCreateAccountDTO;
import adria.sid.ebanckingbackend.ennumerations.EtatCompte;
import adria.sid.ebanckingbackend.entities.Compte;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.repositories.CompteRepository;
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
    private final CompteRepository compteRepository;

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
    public ResponseEntity<String> createAccountForExistingUser(@RequestParam("email") String email, @RequestBody ReqCreateAccountDTO accountDTO, final HttpServletRequest request) {
        // Find the existing user with the provided email
        Optional<UserEntity> existingUserOptional = userRepository.findByEmail(email);

        if (existingUserOptional.isPresent()) {
            // User with the same email already exists, create an account for them
            UserEntity existingUser = existingUserOptional.get();
            Compte newCompte = new Compte();
            newCompte.setId(UUID.randomUUID().toString());
            newCompte.setNature(accountDTO.getNature());
            newCompte.setSolde(accountDTO.getSolde());
            newCompte.setRIB(accountDTO.getRIB());
            newCompte.setNumCompte(accountDTO.getNumCompte());
            newCompte.setDateCreation(accountDTO.getDateCreation());
            newCompte.setDatePeremption(accountDTO.getDatePeremption());
            newCompte.setDerniereDateSuspention(accountDTO.getDerniereDateSuspention());
            newCompte.setDerniereDateBloquage(accountDTO.getDerniereDateBloquage());
            newCompte.setEtatCompte(EtatCompte.BLOCKE); // Set the initial state of the account

            // Add the new account to the existing user's list of accounts
            existingUser.addCompte(newCompte);

            // Save the changes to the existing user (this will also save the new account)
            userRepository.save(existingUser);

            return ResponseEntity.ok("Bravo ! Un compte a été créé pour cet utilisateur. Check your e-mail pour finaliser votre inscription");
        }

        return ResponseEntity.notFound().build();
    }



    public String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
