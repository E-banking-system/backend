package adria.sid.ebanckingbackend.controllers;

import adria.sid.ebanckingbackend.dtos.compte.CompteResDTO;
import adria.sid.ebanckingbackend.exceptions.IdUserIsNotValideException;
import adria.sid.ebanckingbackend.services.compte.CompteService;
import adria.sid.ebanckingbackend.services.operation.virement.VirementService;
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
@RequestMapping("/api/v1/client")
@Tag(name = "Client")
@Slf4j
@AllArgsConstructor
public class ClientController {
    private final CompteService compteService;
    private final VirementService virementService;

    /*@GetMapping("/virements")
    public ResponseEntity<Page<VirementResDTO>> getClientVirements(
            @RequestParam String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<VirementResDTO> virements = virementService.getClientVirements(userId, pageable);
            return ResponseEntity.ok(virements);
        } catch (IdUserIsNotValideException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (InternalError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }*/

    @GetMapping("/comptes")
    public ResponseEntity<Page<CompteResDTO>> getClientComptes(
            @RequestParam String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<CompteResDTO> comptes = compteService.getClientComptes(userId, pageable, keyword);
            return ResponseEntity.ok(comptes);
        } catch (IdUserIsNotValideException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (InternalError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Exception handler to handle InternalError
    @ExceptionHandler(InternalError.class)
    public ResponseEntity<String> handleInternalError(InternalError e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    // Exception handler to handle IdUserIsNotValideException
    @ExceptionHandler(IdUserIsNotValideException.class)
    public ResponseEntity<String> handleIdUserIsNotValideException(IdUserIsNotValideException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
