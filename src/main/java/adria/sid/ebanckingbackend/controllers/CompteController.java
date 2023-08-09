package adria.sid.ebanckingbackend.controllers;

import adria.sid.ebanckingbackend.dtos.compte.*;
import adria.sid.ebanckingbackend.dtos.operation.DepotReqDTO;
import adria.sid.ebanckingbackend.dtos.operation.OperationResDTO;
import adria.sid.ebanckingbackend.dtos.operation.RetraitReqDTO;
import adria.sid.ebanckingbackend.exceptions.*;
import adria.sid.ebanckingbackend.services.compte.CompteService;
import adria.sid.ebanckingbackend.services.operation.changeSolde.ChangeSoldeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/compte")
@Validated
@RequiredArgsConstructor
@Slf4j
public class CompteController {
    final private CompteService compteService;
    final private ChangeSoldeService changeSoldeService;

    @GetMapping("/operations")
    public ResponseEntity<?> getCompteOperations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(required = false) String compteId
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

            Page<OperationResDTO> operationResDTOPage;
            operationResDTOPage = compteService.getCompteOperations(pageable,compteId);

            return ResponseEntity.ok(operationResDTOPage);
        } catch (Exception | CompteNotExistException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> saveCompte(@RequestBody @Valid CompteReqDTO accountDTO) {
        try {
            compteService.ajouterCompte(accountDTO);
            return ResponseEntity.ok("Un compte a été créé pour cet utilisateur. Check your e-mail pour voir les informations sur vos compte");
        } catch (IdUserIsNotValideException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (InternalError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getComptes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(required = false) String keyword
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

            Page<CompteResDTO> comptePage;

            if (keyword != null && !keyword.isEmpty()) {
                comptePage = compteService.searchComptes(pageable, keyword);
            } else {
                comptePage = compteService.getComptes(pageable);
            }

            return ResponseEntity.ok(comptePage);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    @PostMapping("/activer")
    public ResponseEntity<String> activateCompte(@RequestBody ActiverCompteReqDTO activerCompteReqDTO) {
        try {
            compteService.activerCompte(activerCompteReqDTO.getCompteId());
            return ResponseEntity.ok("Compte activé avec succès.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception | NotificationNotSended e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/blocker")
    public ResponseEntity<String> blockCompte(@RequestBody BlockerCompteReqDTO blockCompteReqDTO) {
        try {
            compteService.blockCompte(blockCompteReqDTO.getCompteId());
            return ResponseEntity.ok("Compte bloqué avec succès.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception | NotificationNotSended e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/suspender")
    public ResponseEntity<String> suspendCompte(@RequestBody SuspenderCompteReqDTO suspenderCompteReqDTO) {
        try {
            compteService.suspendCompte(suspenderCompteReqDTO.getCompteId());
            return ResponseEntity.ok("Compte suspendu avec succès.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception | NotificationNotSended e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/demande_suspend")
    public ResponseEntity<String> demandeSuspend(@RequestBody @Valid DemandeSuspendDTO demandeSuspendDTO){
        try {
            compteService.demandeSuspendCompte(demandeSuspendDTO);
            return ResponseEntity.ok("Demande de suspend du compte a été envoyé avec succès.");
        } catch (Exception | NotificationNotSended e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/demande_block")
    public ResponseEntity<String> demandeBlock(@RequestBody @Valid DemandeBlockDTO demandeBlockDTO){
        try {
            compteService.demandeBlockCompte(demandeBlockDTO);
            return ResponseEntity.ok("Demande de block du compte a été envoyé avec succès.");
        } catch (Exception | NotificationNotSended e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/demande_activer")
    public ResponseEntity<String> demandeActiver(@RequestBody @Valid DemandeActivateDTO demandeActivateDTO){
        try {
            compteService.demandeActivateCompte(demandeActivateDTO);
            return ResponseEntity.ok("Demande d'activer du compte a été envoyé avec succès.");
        } catch (Exception | NotificationNotSended e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/depot")
    public ResponseEntity<?> depot(@RequestBody @Valid DepotReqDTO depotReqDTO){
        try {
            changeSoldeService.depot(depotReqDTO);
            return ResponseEntity.ok("Depot d'argent effectué avec succès.");
        } catch (Exception | NotificationNotSended | OperationNotSaved e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/retrait")
    public ResponseEntity<?> retrait(@RequestBody @Valid RetraitReqDTO retraitReqDTO){
        try {
            changeSoldeService.retrait(retraitReqDTO);
            return ResponseEntity.ok("Retrait d'argent effectué avec succès.");
        } catch (Exception | NotificationNotSended e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        } catch (InsufficientBalanceException | OperationNotSaved e) {
            throw new RuntimeException(e);
        }
    }

    // Exception handler to handle IdUserIsNotValideException
    @ExceptionHandler(NotificationNotSended.class)
    public ResponseEntity<String> handleNotificationNotSendedException(NotificationNotSended e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    // Exception handler to handle IdUserIsNotValideException
    @ExceptionHandler(IdUserIsNotValideException.class)
    public ResponseEntity<String> handleIdUserIsNotValideException(IdUserIsNotValideException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    // Exception handler to handle InternalError
    @ExceptionHandler(InternalError.class)
    public ResponseEntity<String> handleInternalError(InternalError e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    // Exception handler to handle InsufficientBalanceException
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<String> handleInsufficientBalanceExceptionException(InsufficientBalanceException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    // Exception handler to handle OperationNotSaved
    @ExceptionHandler(OperationNotSaved.class)
    public ResponseEntity<String> handleOperationNotSavedException(OperationNotSaved e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    // Exception handler to handle CompteNotExistException
    @ExceptionHandler(CompteNotExistException.class)
    public ResponseEntity<String> handleCompteNotExistExceptionException(CompteNotExistException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}