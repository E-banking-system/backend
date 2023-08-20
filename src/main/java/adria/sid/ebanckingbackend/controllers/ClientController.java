package adria.sid.ebanckingbackend.controllers;

import adria.sid.ebanckingbackend.dtos.compte.CompteResDTO;
import adria.sid.ebanckingbackend.exceptions.IdUserIsNotValideException;
import adria.sid.ebanckingbackend.services.compte.CompteService;
import adria.sid.ebanckingbackend.services.notification.NotificationService;
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
@AllArgsConstructor
public class ClientController {
    private final CompteService compteService;
    private final NotificationService notificationService;

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

    @GetMapping("/nbrNotification")
    public int getClientNbrNotification(@RequestParam String userId){
        return notificationService.getNbrNotificationsByUserId(userId);
    }
}
