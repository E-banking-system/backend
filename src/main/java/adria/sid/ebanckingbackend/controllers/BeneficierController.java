package adria.sid.ebanckingbackend.controllers;

import adria.sid.ebanckingbackend.dtos.beneficier.BeneficierReqDTO;
import adria.sid.ebanckingbackend.dtos.beneficier.BeneficierResDTO;
import adria.sid.ebanckingbackend.dtos.compte.CompteReqDTO;
import adria.sid.ebanckingbackend.dtos.compte.CompteResDTO;
import adria.sid.ebanckingbackend.dtos.notification.NotificationResDTO;
import adria.sid.ebanckingbackend.entities.Beneficier;
import adria.sid.ebanckingbackend.exceptions.IdUserIsNotValideException;
import adria.sid.ebanckingbackend.services.beneficiaire.BeneficierService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<String> saveCompte(@RequestBody @Valid BeneficierReqDTO beneficierReqDTO) {
        try {
            beneficierService.ajouterBeneficiair(beneficierReqDTO);
            return ResponseEntity.ok("Un beneficier a été créé.");
        } catch (IdUserIsNotValideException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (InternalError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
