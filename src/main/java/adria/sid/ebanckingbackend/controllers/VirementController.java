package adria.sid.ebanckingbackend.controllers;

import adria.sid.ebanckingbackend.dtos.operation.VirementPermaReqDTO;
import adria.sid.ebanckingbackend.dtos.operation.VirementUnitReqDTO;
import adria.sid.ebanckingbackend.dtos.otp.OtpReqStepOneDTO;
import adria.sid.ebanckingbackend.dtos.otp.OtpReqStepTwoDTO;
import adria.sid.ebanckingbackend.exceptions.*;
import adria.sid.ebanckingbackend.services.operation.virement.VirementService;
import adria.sid.ebanckingbackend.services.operation.virement.otpTransferVerification.OtpTransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/virement")
public class VirementController {
    final private VirementService virementService;
    final private OtpTransferService otpTransferService;

    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@RequestBody @Valid OtpReqStepTwoDTO otpReqStepTwoDTO){
        try {
            otpTransferService.validateOtpTransferResetToken(otpReqStepTwoDTO);
            return ResponseEntity.ok("OTP verified with success");
        } catch (IdUserIsNotValideException | InvalidTransferOtpCode | ExpiredTransferToken e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/otp")
    public ResponseEntity<String> otp(@RequestBody @Valid OtpReqStepOneDTO otpReqStepOneDTO){
        otpTransferService.createOtpTransferResetTokenForUser(otpReqStepOneDTO);
        return ResponseEntity.ok("OTP created with success");
    }

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
        } catch (NotificationNotSended | InsufficientBalanceException | OperationNotSaved e) {
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

}
