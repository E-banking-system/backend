package adria.sid.ebanckingbackend.controllers;


import adria.sid.ebanckingbackend.dtos.CompteReqDTO;
import adria.sid.ebanckingbackend.dtos.CompteResDTO;
import adria.sid.ebanckingbackend.services.compte.CompteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<Page<CompteResDTO>> getAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        // Create a Pageable object to represent the pagination information
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        // Retrieve the paginated accounts data from the service
        Page<CompteResDTO> comptePage = compteService.getAccounts(pageable);

        return ResponseEntity.ok(comptePage);
    }
}
