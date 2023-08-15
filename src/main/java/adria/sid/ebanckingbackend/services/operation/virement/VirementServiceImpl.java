package adria.sid.ebanckingbackend.services.operation.virement;

import adria.sid.ebanckingbackend.dtos.operation.VirementPermaReqDTO;
import adria.sid.ebanckingbackend.dtos.operation.VirementUnitReqDTO;
import adria.sid.ebanckingbackend.ennumerations.EtatCompte;
import adria.sid.ebanckingbackend.entities.*;
import adria.sid.ebanckingbackend.exceptions.*;
import adria.sid.ebanckingbackend.mappers.VirementMapper;
import adria.sid.ebanckingbackend.repositories.*;
import adria.sid.ebanckingbackend.security.otpTransferToken.OtpTransferRepository;
import adria.sid.ebanckingbackend.security.otpTransferToken.OtpTransferToken;
import adria.sid.ebanckingbackend.services.notification.OperationNotificationService;
import adria.sid.ebanckingbackend.utils.codeGenerators.CodeGenerator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class VirementServiceImpl implements VirementService{
    final private CompteRepository compteRepository;
    final private VirementProgrammeRepository virementProgrammeRepository;
    final private BeneficierRepository beneficierRepository;
    final private VirementPermanentRepository virementPermanentRepository;
    final private CodeGenerator codeGenerator;
    final private OperationNotificationService operationNotificationService;
    final private VirementMapper virementMapper;
    final private VirementUnitaireRepository virementUnitaireRepository;
    final private OtpTransferRepository otpTransferRepository;
    final private UserRepository userRepository;


    @Transactional
    @Override
    public void virementUnitaire(VirementUnitReqDTO virementUnitReqDTO) throws InsufficientBalanceException, MontantNotValide, CompteNotExistException, NotificationNotSended, OperationNotSaved, OtpTokenIsNotValid, OtpTokenIsNotVerified {
        UserEntity userEntity=userRepository.findById(virementUnitReqDTO.getUserId()).orElse(null);
        if(userEntity == null){
            throw new IdUserIsNotValideException("This user is not exists");
        }

        OtpTransferToken otpTransferToken=otpTransferRepository.findByUserIdAndToken(virementUnitReqDTO.getUserId(),virementUnitReqDTO.getOtpToken());
        if(otpTransferToken == null){
            throw new OtpTokenIsNotValid("This token is not valid");
        }

        if(!otpTransferToken.getVerified()){
            throw new OtpTokenIsNotVerified("This token is not verified");
        }

        if (virementUnitReqDTO.getMontant() < 100) {
            throw new MontantNotValide("The amount must be more than 100 dirhams");
        }

        // Get client's account details from the repository
        Compte clientCompte = compteRepository.getCompteByNumCompte(virementUnitReqDTO.getNumCompteClient());
        if (clientCompte == null) {
            log.warn("The client with this ID is not valid");
            throw new CompteNotExistException("The client with this ID is not valid");
        }

        // Get beneficiary's account details from the repository
        Compte beneficierCompte = compteRepository.getCompteByNumCompte(virementUnitReqDTO.getNumCompteBeneficier());
        if (beneficierCompte == null) {
            log.warn("The beneficial with this ID is not valid");
            throw new CompteNotExistException("The beneficial with this ID is not valid");
        }

        // Validate the state of the client's account (must be ACTIVE)
        if(!clientCompte.getEtatCompte().equals(EtatCompte.ACTIVE)){
            log.warn("This client account is not active");
            throw new CompteNotActiveException("This client account is not active");
        }

        // Validate the state of the beneficiary's account (must be ACTIVE)
        if(!beneficierCompte.getEtatCompte().equals(EtatCompte.ACTIVE)){
            log.warn("This beneficial account is not active");
            throw new CompteNotActiveException("This beneficial account is not active");
        }

        creditVirementUnitaire(clientCompte, virementUnitReqDTO.getMontant());
        debit(beneficierCompte, virementUnitReqDTO.getMontant());
        operationNotificationService.sendVirementUnitNotificationToClient(clientCompte,virementUnitReqDTO.getMontant());
        operationNotificationService.sendVirementUnitNotificationToBeneficier(beneficierCompte,virementUnitReqDTO.getMontant());

        try {
            VirementUnitaire virementUnitaire=new VirementUnitaire();
            virementUnitaire.setId(UUID.randomUUID().toString());
            virementUnitaire.setMontant(virementUnitReqDTO.getMontant());
            virementUnitaire.setDateOperation(new Date());
            virementUnitaire.setCompte(clientCompte);

            Beneficier beneficier=beneficierRepository.getBeneficiersByNumCompte(beneficierCompte.getNumCompte());
            virementUnitaire.setBeneficier(beneficier);
            virementUnitaireRepository.save(virementUnitaire);
            log.info("Unit transfer is saved with success");
        } catch (Exception e){
            log.warn("This unit transfer is not saved");
            throw new OperationNotSaved("This unit transfer is not saved");
        }
    }

    @Transactional
    public void creditVirementUnitaire(Compte clientCompte, double montant) throws InsufficientBalanceException {
        double newSolde=clientCompte.getSolde()-montant;
        if(newSolde>=0){
            clientCompte.setSolde(newSolde);
            compteRepository.save(clientCompte);
            log.info("Credit unit transfer is saved with success");
        } else {
            log.warn("Credit operation is not possible because this amount is more than the current balance");
            throw new InsufficientBalanceException("Credit operation is not possible because this amount is more than the current balance");
        }
    }

    @Transactional
    public void creditVirementPermanent(Compte clientCompte, double montant) throws NotificationNotSended {
        double newSolde=clientCompte.getSolde()-montant;
        if(newSolde>=0) {
            clientCompte.setSolde(newSolde);
            compteRepository.save(clientCompte);
            log.info("Credit permanently transfer is saved with success");
        } else{
            log.warn("Credit operation is not possible because this amount is more than the current balance");
            operationNotificationService.sendSoldeInsifisantCompteNotificationToClient(clientCompte.getNumCompte(),clientCompte.getUser());
        }
    }

    @Transactional
    public void debit(Compte beneficierCompte, double montant){
        double newSolde= beneficierCompte.getSolde()+montant;
        beneficierCompte.setSolde(newSolde);
        log.info("Debit is saved with success");
        compteRepository.save(beneficierCompte);
    }

    // Method to perform a scheduled money transfer (Virement Permanent)
    @Override
    @Transactional
    public void virementProgramme(VirementPermaReqDTO virementPermaReqDTO) throws DatesVirementPermanentAreNotValide, CompteNotExistException {
        if (virementPermaReqDTO.getPremierDateExecution().compareTo(virementPermaReqDTO.getDateFinExecution()) > 0) {
            log.warn("Programed dates for this transfer are not valid");
            throw new DatesVirementPermanentAreNotValide("Programed dates for this transfer are not valid");
        }

        // Get client's account details from the repository
        Compte clientCompte = compteRepository.getCompteByNumCompte(virementPermaReqDTO.getNumCompteClient());
        if (clientCompte == null) {
            log.warn("The client with this ID is not valid");
            throw new CompteNotExistException("The client with this ID is not valid");
        }

        // Get beneficiary's account details from the repository
        Compte beneficierCompte = compteRepository.getCompteByNumCompte(virementPermaReqDTO.getNumCompteBeneficier());
        if (beneficierCompte == null) {
            log.warn("The beneficial with this ID is not valid");
            throw new CompteNotExistException("The beneficial with this ID is not valid");
        }

        // Validate the state of the client's account (must be ACTIVE)
        if(!clientCompte.getEtatCompte().equals(EtatCompte.ACTIVE)){
            log.warn("This client account is not active");
            throw new CompteNotActiveException("This client account is not active");
        }

        // Validate the state of the beneficiary's account (must be ACTIVE)
        if(!beneficierCompte.getEtatCompte().equals(EtatCompte.ACTIVE)){
            log.warn("This beneficial account is not active");
            throw new CompteNotActiveException("This beneficial account is not active");
        }

        // Generate the list of execution dates for the scheduled transfer
        List<Date> datesExecution = codeGenerator.genererDatesVirementPermanentVersionTest(virementPermaReqDTO.getPremierDateExecution(),virementPermaReqDTO.getDateFinExecution(),virementPermaReqDTO.getFrequence());

        // Add the execution dates to the database as VirementProgramme entries
        for (Date dateExecution : datesExecution) {
            VirementProgramme virementProgrammeExecution = new VirementProgramme();
            virementProgrammeExecution.setId(UUID.randomUUID().toString());
            virementProgrammeExecution.setDateExecution(dateExecution);
            virementProgrammeExecution.setMontant(virementPermaReqDTO.getMontant());
            virementProgrammeExecution.setFrequence(virementPermaReqDTO.getFrequence());
            virementProgrammeExecution.setNumCompteClient(virementPermaReqDTO.getNumCompteClient());
            virementProgrammeExecution.setNumCompteBeneficier(virementPermaReqDTO.getNumCompteBeneficier());

            // Save the scheduled transfer in the database
            virementProgrammeRepository.save(virementProgrammeExecution);
            log.info("Programmed transfer is done with success");
        }
    }

    // Method to perform immediate execution of scheduled money transfers
    @Transactional
    public void virementPermanent(VirementProgramme virementProgramme) throws NotificationNotSended, OperationNotSaved {
        // Get client's and beneficiary's account details from the repository
        Compte clientCompte = compteRepository.getCompteByNumCompte(virementProgramme.getNumCompteClient());
        // Validate the state of the client's account (must be ACTIVE)
        if (!clientCompte.getEtatCompte().equals(EtatCompte.ACTIVE)) {
            operationNotificationService.sendClientCompteNotActiveNotificationToClient(clientCompte);
        }


        Compte beneficierCompte = compteRepository.getCompteByNumCompte(virementProgramme.getNumCompteBeneficier());
        // Validate the state of the beneficiary's account (must be ACTIVE)
        if(!beneficierCompte.getEtatCompte().equals(EtatCompte.ACTIVE)){
            operationNotificationService.sendBeneficierCompteNotActiveNotificationToClient(clientCompte,beneficierCompte);
        }

        double newClientSolde=clientCompte.getSolde()-virementProgramme.getMontant();
        if(newClientSolde>0) {
            creditVirementPermanent(clientCompte, virementProgramme.getMontant());
            debit(beneficierCompte, virementProgramme.getMontant());
        } else {
            operationNotificationService.sendSoldeInsifisantCompteNotificationToClient(clientCompte.getNumCompte(),clientCompte.getUser());
        }

        try {
            // Save the transfer details in the virementPermanantRepository
            VirementPermanant virementPermanant=virementMapper.fromVirementProgrammeToVirementPermanent(virementProgramme);
            virementPermanant.setFrequence(virementProgramme.getFrequence());
            virementPermanant.setCompte(clientCompte);
            virementPermanant.setDateOperation(new Date());

            Beneficier beneficier=beneficierRepository.getBeneficiersByNumCompte(virementProgramme.getNumCompteBeneficier());
            virementPermanant.setBeneficier(beneficier);
            virementPermanentRepository.save(virementPermanant);
            log.info("Permanently transfer is saved with success");
        } catch (Exception e){
            log.warn("Permanently transfer is not saved with success");
            throw new OperationNotSaved("Permanently transfer is not saved with success");
        }

        operationNotificationService.sendVirementPermanentNotificationToClientCompte(clientCompte, virementProgramme.getMontant());
        operationNotificationService.sendVirementPermanentNotificationToBeneficierCompte(beneficierCompte, virementProgramme.getMontant());
    }

    @Scheduled(fixedRateString = "${myapp.scheduled-task.fixed-rate}") // Run every 5000 milliseconds (5 seconds)
    public void effectuerVirementProgramme() throws NotificationNotSended, OperationNotSaved {
        // Find pending scheduled transfers that are due for execution
        List<VirementProgramme> virementsProgramme = virementProgrammeRepository.findPendingVirements(new Date());
        // Execute each pending scheduled transfer and update its status
        if (virementsProgramme.size() > 0) {
            for (VirementProgramme virementProgramme : virementsProgramme) {
                if (!virementProgramme.isEffectuer()) {
                    // Execute the pending scheduled transfer immediately
                    virementPermanent(virementProgramme);
                }
                // Mark the scheduled transfer as executed
                virementProgramme.setEffectuer(true);
                virementProgrammeRepository.save(virementProgramme);
                log.info("Programmed transfer is saved with success on reel time : "+new Date());
            }
        }
    }
}
