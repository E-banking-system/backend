package adria.sid.ebanckingbackend.controllers;

import adria.sid.ebanckingbackend.dtos.beneficier.BeneficierResDTO;
import adria.sid.ebanckingbackend.dtos.compte.CompteResDTO;
import adria.sid.ebanckingbackend.dtos.notification.NotificationResDTO;
import adria.sid.ebanckingbackend.entities.Beneficier;
import adria.sid.ebanckingbackend.exceptions.IdUserIsNotValideException;
import adria.sid.ebanckingbackend.services.beneficiaire.BeneficierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/beneficier")
@Validated
@RequiredArgsConstructor
@Slf4j
public class BeneficierController {
    final private BeneficierService beneficierService;

    @GetMapping
    public ResponseEntity<?> geteBeneficierByClientId(
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
}
