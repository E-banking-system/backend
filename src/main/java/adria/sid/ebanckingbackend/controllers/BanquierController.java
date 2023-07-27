package adria.sid.ebanckingbackend.controllers;

import adria.sid.ebanckingbackend.dtos.ReqCreateAccountDTO;
import adria.sid.ebanckingbackend.services.CompteService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/banquier")
@Tag(name = "Banquier")
@CrossOrigin("*")
@RequiredArgsConstructor
public class BanquierController {

    private final CompteService compteService;

    @GetMapping
    @PreAuthorize("hasAuthority('banquier:read')")
    public String get() {
        return "GET:: banquier controller";
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

    @PostMapping
    @PreAuthorize("hasAuthority('banquier:banquier_suite_registration_client')")
    public ResponseEntity<String> createAccountForExistingUser(@RequestBody ReqCreateAccountDTO accountDTO) {
        compteService.createAccountForExistingUserAndSendEmail(accountDTO);
        return ResponseEntity.ok("Un compte a été créé pour cet utilisateur. Check your e-mail pour voir les informations sur vos compte");
    }
}
