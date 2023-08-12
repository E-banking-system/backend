package adria.sid.ebanckingbackend.controllers;

import adria.sid.ebanckingbackend.dtos.beneficier.BeneficierReqDTO;
import adria.sid.ebanckingbackend.dtos.beneficier.BeneficierResDTO;
import adria.sid.ebanckingbackend.exceptions.BeneficierEmailIsNotExiste;
import adria.sid.ebanckingbackend.exceptions.CompteNotExistException;
import adria.sid.ebanckingbackend.exceptions.IdUserIsNotValideException;
import adria.sid.ebanckingbackend.services.beneficiaire.BeneficierService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/beneficier")
@Validated
@RequiredArgsConstructor
@Slf4j
public class BeneficierController {
    final private BeneficierService beneficierService;

    @GetMapping
    public ResponseEntity<?> getBeneficierByClientId(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam String clientId){
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

            Page<BeneficierResDTO> beneficierResDTOS = beneficierService.getBeneficiersByClientId(pageable,clientId);

            return ResponseEntity.ok(beneficierResDTOS);
        } catch (IdUserIsNotValideException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> saveBeneficier(@RequestBody @Valid BeneficierReqDTO beneficierReqDTO) {
        try {
            beneficierService.ajouterBeneficiair(beneficierReqDTO);
            return ResponseEntity.ok("Un beneficier a été créé.");
        } catch (IdUserIsNotValideException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (InternalError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (CompteNotExistException e) {
            throw new RuntimeException(e);
        } catch (BeneficierEmailIsNotExiste e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{beneficierId}")
    public ResponseEntity<String> updateBeneficier(
            @PathVariable String beneficierId,
            @RequestBody @Valid BeneficierReqDTO beneficierReqDTO) {
        try {
            beneficierService.modifierBeneficier(beneficierReqDTO, beneficierId);
            return ResponseEntity.ok("Beneficier a été modifié");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IdUserIsNotValideException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (CompteNotExistException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{beneficierId}")
    public ResponseEntity<String> deleteBeneficier(@PathVariable String beneficierId) {
        try {
            beneficierService.supprimerBeneficier(beneficierId);
            return ResponseEntity.ok("Beneficier a été supprimé");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
