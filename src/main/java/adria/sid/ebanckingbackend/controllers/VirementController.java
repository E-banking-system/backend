package adria.sid.ebanckingbackend.controllers;

import adria.sid.ebanckingbackend.dtos.operation.VirementPermaReqDTO;
import adria.sid.ebanckingbackend.dtos.operation.VirementUnitReqDTO;
import adria.sid.ebanckingbackend.exceptions.*;
import adria.sid.ebanckingbackend.services.operation.virement.VirementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/virement")
public class VirementController {
    final private VirementService virementService;

    @PostMapping("/unitaire")
    public ResponseEntity<String> effectuerVirementUnitaire(@RequestBody @Valid VirementUnitReqDTO virementUnitReqDTO) {
        try {
            virementService.virementUnitaire(virementUnitReqDTO);
            return ResponseEntity.ok("Virement effectué avec success : "+virementUnitReqDTO.getMontant());
        } catch (IllegalArgumentException |
                 CompteNotExistException | MontantNotValide e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        } catch (NotificationNotSended | InsufficientBalanceException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/permanent")
    public ResponseEntity<String> effectuerVirementPermanent(@RequestBody @Valid VirementPermaReqDTO virementPermanentReqDTO) {
        try {
            virementService.virementProgramme(virementPermanentReqDTO);
            return ResponseEntity.ok("Virement permanent programé avec succèss");
        } catch (IllegalArgumentException | DatesVirementPermanentAreNotValide e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        } catch (CompteNotExistException e) {
            throw new RuntimeException(e);
        }
    }

    // Exception handler to handle DatesVirementPermanentAreNotValide
    @ExceptionHandler(DatesVirementPermanentAreNotValide.class)
    public ResponseEntity<String> handleDatesVirementPermanentAreNotValide(DatesVirementPermanentAreNotValide e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    // Exception handler to handle ClientIsNotExistException
    @ExceptionHandler(ClientIsNotExistException.class)
    public ResponseEntity<String> handleClientIsNotExistException(ClientIsNotExistException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    // Exception handler to handle BeneficierIsNotExistException
    @ExceptionHandler(BeneficierIsNotExistException.class)
    public ResponseEntity<String> handleBeneficierIsNotExistException(BeneficierIsNotExistException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    // Exception handler to handle CompteNotExistException
    @ExceptionHandler(CompteNotExistException.class)
    public ResponseEntity<String> handleCompteNotExistException(CompteNotExistException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    // Exception handler to handle InternalError
    @ExceptionHandler(InternalError.class)
    public ResponseEntity<String> handleInternalError(InternalError e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    // Exception handler to handle IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
    }

    // Exception handler to handle IdUserIsNotValideException
    @ExceptionHandler(NotificationNotSended.class)
    public ResponseEntity<String> handleNotificationNotSendedException(NotificationNotSended e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    // Exception handler to handle InsufficientBalanceException
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<String> handleInsufficientBalanceExceptionException(InsufficientBalanceException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
