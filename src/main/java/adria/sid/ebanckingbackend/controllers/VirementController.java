package adria.sid.ebanckingbackend.controllers;

import adria.sid.ebanckingbackend.dtos.compte.ChangeSoldeReqDTO;
import adria.sid.ebanckingbackend.dtos.virement.VirementUnitReqDTO;
import adria.sid.ebanckingbackend.exceptions.BeneficierIsNotExistException;
import adria.sid.ebanckingbackend.exceptions.ClientIsNotExistException;
import adria.sid.ebanckingbackend.exceptions.CompteNotExistException;
import adria.sid.ebanckingbackend.services.virement.VirementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/virement")
public class VirementController {
    final private VirementService virementService;
    @PostMapping("/unitaire")
    public ResponseEntity<String> effectuerVirementUnitaire(@RequestBody @Valid VirementUnitReqDTO virementUnitReqDTO) {
        try {
            virementService.effectuerVirementUnitaire(virementUnitReqDTO);
            return ResponseEntity.ok("Viremnt effectué avec success : "+virementUnitReqDTO.getMontant());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        } catch (ClientIsNotExistException | BeneficierIsNotExistException | CompteNotExistException e) {
            throw new RuntimeException(e);
        }
    }
}
