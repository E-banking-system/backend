package adria.sid.ebanckingbackend.controllers;

import adria.sid.ebanckingbackend.dtos.client.ClientResDTO;
import adria.sid.ebanckingbackend.dtos.compte.CompteResDTO;
import adria.sid.ebanckingbackend.dtos.virement.VirementResDTO;
import adria.sid.ebanckingbackend.exceptions.IdUserIsNotValideException;
import adria.sid.ebanckingbackend.services.authentification.AuthenticationService;
import adria.sid.ebanckingbackend.services.compte.CompteService;
import adria.sid.ebanckingbackend.services.virement.VirementService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/banquier")
@Tag(name = "Banquier")
@Slf4j
@AllArgsConstructor
public class BanquierController {
    private final AuthenticationService authenticationService;

    @GetMapping("/clients")
    public ResponseEntity<Page<ClientResDTO>> getClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<ClientResDTO> clients = authenticationService.getClientVirements(pageable);
            return ResponseEntity.ok(clients);
        } catch (InternalError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Exception handler to handle InternalError
    @ExceptionHandler(InternalError.class)
    public ResponseEntity<String> handleInternalError(InternalError e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

}
