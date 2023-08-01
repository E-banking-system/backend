package adria.sid.ebanckingbackend.controllers;


import adria.sid.ebanckingbackend.dtos.compte.ChangeSoldeReqDTO;
import adria.sid.ebanckingbackend.dtos.compte.*;
import adria.sid.ebanckingbackend.exceptions.IdUserIsNotValideException;
import adria.sid.ebanckingbackend.services.compte.CompteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/compte")
@Validated
@RequiredArgsConstructor
@Slf4j
public class CompteController {
    private final CompteService compteService;

    @PreAuthorize("hasAuthority('banquier:creer_compte')")
    @PostMapping
    public ResponseEntity<String> saveCompte(@RequestBody @Valid CompteReqDTO accountDTO) {
        try {
        compteService.ajouterCompte(accountDTO);
        return ResponseEntity.ok("Un compte a été créé pour cet utilisateur. Check your e-mail pour voir les informations sur vos compte");
        } catch (IdUserIsNotValideException e){
            return ResponseEntity.badRequest().body("Id user is not valide");
        } catch (InternalError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getComptes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

            Page<CompteResDTO> comptePage = compteService.getComptes(pageable);

            return ResponseEntity.ok(comptePage);
        } catch (Exception e){
            return ResponseEntity.internalServerError().body("INTERNAL SERVER ERROR");
        }
    }

    @PostMapping("/activer")
    public ResponseEntity<String> activateCompte(@RequestBody ActiverCompteReqDTO activerCompteReqDTO) {
        try {
            compteService.activerCompte(activerCompteReqDTO.getCompteId());
            return ResponseEntity.ok("Compte activé avec succès.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Compte non trouvé avec l'ID donné.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Une erreur est survenue lors de l'activation du compte.");
        }
    }

    @PostMapping("/blocker")
    public ResponseEntity<String> blockCompte(@RequestBody BlockerCompteReqDTO blockCompteReqDTO) {
        try {
            compteService.blockCompte(blockCompteReqDTO.getCompteId());
            return ResponseEntity.ok("Compte bloqué avec succès.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Compte non trouvé avec l'ID donné.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Une erreur est survenue lors du blocage du compte.");
        }
    }

    @PostMapping("/suspender")
    public ResponseEntity<String> suspendCompte(@RequestBody SuspenderCompteReqDTO suspenderCompteReqDTO) {
        try {
            compteService.suspendCompte(suspenderCompteReqDTO.getCompteId());
            return ResponseEntity.ok("Compte suspendu avec succès.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Compte non trouvé avec l'ID donné.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Une erreur est survenue lors de la suspension du compte.");
        }
    }

    @PostMapping("/change_solde")
    public ResponseEntity<String> changeSolde(@RequestBody @Valid ChangeSoldeReqDTO changeSoldeReqDTO) {
        try {
            compteService.changeSolde(changeSoldeReqDTO.getCompteId(), changeSoldeReqDTO.getMontant());
            return ResponseEntity.ok("Solde modifié avec succès.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Une erreur est survenue lors de la modification du solde.");
        }
    }

    @PostMapping("/demande_suspend")
    public ResponseEntity<String> demandeSuspend(@RequestBody @Valid DemandeSuspendDTO demandeSuspendDTO){
        try {
            compteService.demandeSuspendCompte(demandeSuspendDTO);
            return ResponseEntity.ok("Demande de suspend du compte a été envoyé avec succès.");
        } catch (Exception e){
            return ResponseEntity.internalServerError().body("Une erreur est survenue lors de la modification du solde.");
        }
    }

    @PostMapping("/demande_block")
    public ResponseEntity<String> demandeBlock(@RequestBody @Valid DemandeBlockDTO demandeBlockDTO){
        try {
            compteService.demandeBlockCompte(demandeBlockDTO);
            return ResponseEntity.ok("Demande de block du compte a été envoyé avec succès.");
        } catch (Exception e){
            return ResponseEntity.internalServerError().body("Une erreur est survenue lors de la modification du solde.");
        }
    }

    @PostMapping("/demande_activer")
    public ResponseEntity<String> demandeActiver(@RequestBody @Valid DemandeActivateDTO demandeActivateDTO){
        try {
            compteService.demandeActivateCompte(demandeActivateDTO);
            return ResponseEntity.ok("Demande d'activer du compte a été envoyé avec succès.");
        } catch (Exception e){
            return ResponseEntity.internalServerError().body("Une erreur est survenue lors de la modification du solde.");
        }
    }
}