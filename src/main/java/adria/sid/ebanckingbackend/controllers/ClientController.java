package adria.sid.ebanckingbackend.controllers;

import adria.sid.ebanckingbackend.dtos.compte.CompteResDTO;
import adria.sid.ebanckingbackend.exceptions.IdUserIsNotValideException;
import adria.sid.ebanckingbackend.services.compte.CompteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/client")
@Tag(name = "Client")
@CrossOrigin("*")
@Slf4j
@AllArgsConstructor
public class ClientController {
    private final CompteService compteService;
    @GetMapping("/comptes")
    public ResponseEntity<?> getClientComptes(
            @RequestParam String userId
    ) {
        try {
            List<CompteResDTO> comptes = compteService.getClientComptes(userId);
            return ResponseEntity.ok(comptes);
        } catch (IdUserIsNotValideException e) {
            return ResponseEntity.badRequest().body("Invalid user ID: " + userId);
        } catch (InternalError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
