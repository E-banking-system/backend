package adria.sid.ebanckingbackend.controllers;

import adria.sid.ebanckingbackend.dtos.ReqCreateAccountDTO;
import adria.sid.ebanckingbackend.entities.Compte;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.repositories.CompteRepository;
import adria.sid.ebanckingbackend.repositories.UserRepository;
import adria.sid.ebanckingbackend.security.emailToken.VerificationTokenRepository;
import adria.sid.ebanckingbackend.services.AuthenticationService;
import adria.sid.ebanckingbackend.services.CompteService;
import adria.sid.ebanckingbackend.utils.HtmlCodeGenerator;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/banquier")
@Tag(name = "Banquier")
@CrossOrigin("*")
@RequiredArgsConstructor
public class BanquierController {

    private final AuthenticationService userService;
    private final UserRepository userRepository;
    private final CompteService compteService;

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
    public ResponseEntity<String> createAccountForExistingUser(@RequestBody ReqCreateAccountDTO accountDTO, final HttpServletRequest request) {

        Optional<UserEntity> existingUserOptional = userRepository.findByEmail(accountDTO.getEmail());

        if (existingUserOptional.isPresent()) {

            UserEntity existingUser = existingUserOptional.get();

            Compte newCompte = compteService.createAccountForExistingUser(accountDTO);

            existingUser.addCompte(newCompte);

            userRepository.save(existingUser);

            return ResponseEntity.ok("Un compte a été créé pour cet utilisateur. Check your e-mail pour voir les informations sur vos compte");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("aucun utilisateur avec cet email");
    }



    public String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
