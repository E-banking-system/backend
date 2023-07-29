package adria.sid.ebanckingbackend.controllers;


import adria.sid.ebanckingbackend.dtos.CompteReqDTO;
import adria.sid.ebanckingbackend.dtos.CompteResDTO;
import adria.sid.ebanckingbackend.services.compte.CompteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/compte")
@CrossOrigin("*")
public class CompteController {
    private final CompteService compteService;

    @PostMapping
    @PreAuthorize("hasAuthority('banquier:banquier_suite_registration_client')")
    public ResponseEntity<String> createAccountForExistingUser(@RequestBody @Valid CompteReqDTO accountDTO) {
        try {
            compteService.createAccountForExistingUserAndSendEmail(accountDTO);
            return ResponseEntity.ok("Un compte a été créé pour cet utilisateur. Check your e-mail pour voir les informations sur vos compte");
        } catch (InternalError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("hasAuthority('banquier:get_accounts')")
    public ResponseEntity<List<CompteResDTO>> getAccounts() {
        List<CompteResDTO> comptes = compteService.getAccounts();
        return ResponseEntity.ok(comptes);
    }
}
