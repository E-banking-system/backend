package adria.sid.ebanckingbackend.services.virement;

import adria.sid.ebanckingbackend.dtos.virement.VirementPermanentReqDTO;
import adria.sid.ebanckingbackend.dtos.virement.VirementUnitReqDTO;
import adria.sid.ebanckingbackend.ennumerations.EtatCompte;
import adria.sid.ebanckingbackend.ennumerations.OPType;
import adria.sid.ebanckingbackend.entities.*;
import adria.sid.ebanckingbackend.exceptions.*;
import adria.sid.ebanckingbackend.mappers.VirementMapper;
import adria.sid.ebanckingbackend.repositories.*;
import adria.sid.ebanckingbackend.services.compte.CompteService;
import adria.sid.ebanckingbackend.services.notification.NotificationCompteService;
import adria.sid.ebanckingbackend.utils.codeGenerators.CodeGenerator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class VirementServiceImpl implements VirementService{
    final private CompteRepository compteRepository;
    final private CompteService compteService;
    final private ViremenProgrammeRepository viremenProgrammeRepository;
    final private VirementPermanantRepository virementPermanantRepository;
    final private VirementUnitaireRepository virementUnitaireRepository;
    final private VirementMapper virementMapper;
    final private NotificationCompteService notificationServiceVirement;
    final private CodeGenerator codeGenerator;
    final private BeneficierRepository beneficierRepository;

    // Method to perform a one-time money transfer (Virement Unitaire)
    @Transactional
    @Override
    public void effectuerVirementUnitaire(VirementUnitReqDTO viremenentReqDTO)
            throws CompteNotExistException, MontantNotValide {
        // Validate the amount for the money transfer
        if (viremenentReqDTO.getMontant() < 100) {
            throw new MontantNotValide("Ce montant n'est pas valide");
        }

        // Get client's account details from the repository
        Compte clientCompte = compteRepository.getCompteByNumCompte(viremenentReqDTO.getNumCompteClient());
        if (clientCompte == null) {
            throw new CompteNotExistException("Le compte client n'est pas valide");
        }

        // Get beneficiary's account details from the repository
        Compte beneficierCompte = compteRepository.getCompteByNumCompte(viremenentReqDTO.getNumCompteBeneficier());
        if (beneficierCompte == null) {
            throw new CompteNotExistException("Le compte beneficier n'est pas valide");
        }

        // Validate the state of the client's account (must be ACTIVE)
        if (validateCompteState(clientCompte, EtatCompte.ACTIVE, viremenentReqDTO.getNumCompteClient())) {
            throw new CompteNotActiveException("Ce compte client n'est pas active");
        }

        // Validate the state of the beneficiary's account (must be ACTIVE)
        if (validateCompteState(beneficierCompte, EtatCompte.ACTIVE, viremenentReqDTO.getNumCompteBeneficier())) {
            throw new CompteNotActiveException("Ce compte beneficier n'est pas active");
        }

        // Deduct the amount from the client's account and add it to the beneficiary's account
        compteService.changeSolde(clientCompte.getNumCompte(), -viremenentReqDTO.getMontant(), OPType.VIREMENT_UNITAIRE);
        compteService.changeSolde(beneficierCompte.getNumCompte(), viremenentReqDTO.getMontant(), OPType.VIREMENT_UNITAIRE);

        // Save the transfer details in the virementUnitaireRepository
        VirementUnitaire virementUnitaire = virementMapper.fromVirementReqDTOToVirementUnitaire(viremenentReqDTO);
        virementUnitaire.setUser(clientCompte.getUser());

        Beneficier beneficier=beneficierRepository.getBeneficiersByNumCompte(beneficierCompte.getNumCompte());
        virementUnitaire.setBeneficier(beneficier);

        virementUnitaireRepository.save(virementUnitaire);

        // Print success message
        System.out.println("Virement effectué avec succès");
    }

    // Method to perform a scheduled money transfer (Virement Permanent)
    @Override
    public void effectuerVirementPermanent(VirementPermanentReqDTO virementPermanentReqDTO) throws DatesVirementPermanentAreNotValide, CompteNotExistException {
        // Validate the execution dates for the scheduled transfer
        if (virementPermanentReqDTO.getPremierDateExecution().compareTo(virementPermanentReqDTO.getDateFinExecution()) > 0) {
            throw new DatesVirementPermanentAreNotValide("Les dates pour programmé ce virement ne sont pas valide");
        }

        Compte clientCompte = compteRepository.getCompteByNumCompte(virementPermanentReqDTO.getNumCompteClient());
        if(!(clientCompte.getEtatCompte() == EtatCompte.ACTIVE)){
            throw new CompteNotExistException("Le numéro de compte du client n'est pas valide");
        }

        Compte beneficierCompte = compteRepository.getCompteByNumCompte(virementPermanentReqDTO.getNumCompteBeneficier());
        if(!(beneficierCompte.getEtatCompte() == EtatCompte.ACTIVE)){
            throw new CompteNotExistException("Le numéro de compte du beneficier n'est pas valide");
        }

        // Convert the scheduled transfer details to VirementProgramme
        VirementProgramme virementProgramme = virementMapper.fromVirementPermanentReqDTOToVirementProgramme(virementPermanentReqDTO);

        // Generate the list of execution dates for the scheduled transfer
        List<Date> datesExecution = codeGenerator.genererDatesVirementPermanent(virementPermanentReqDTO.getPremierDateExecution(),virementPermanentReqDTO.getDateFinExecution(),virementPermanentReqDTO.getFrequence());

        // Add the execution dates to the database as VirementProgramme entries
        for (Date dateExecution : datesExecution) {
            VirementProgramme virementProgrammeExecution = new VirementProgramme();
            virementProgrammeExecution.setId(UUID.randomUUID().toString());
            virementProgrammeExecution.setDateExecution(dateExecution);
            virementProgrammeExecution.setEffectuer(false);
            virementProgrammeExecution.setMontant(virementProgramme.getMontant());
            virementProgrammeExecution.setFrequence(virementProgramme.getFrequence());
            virementProgrammeExecution.setNumCompteClient(virementProgramme.getNumCompteClient());
            virementProgrammeExecution.setNumCompteBeneficier(virementProgramme.getNumCompteBeneficier());

            // Save the scheduled transfer in the database
            viremenProgrammeRepository.save(virementProgrammeExecution);
        }

        System.out.println("Virement programmé avec succès");
    }

    // Helper method to validate the existence of a Compte in the database
    private boolean validateCompteExistence(Compte compte, String numCompte) {
        if (compte == null) {
            System.out.println("Ce numéro de compte n'existe pas : " + numCompte);
            return false;
        }
        return true;
    }

    // Helper method to validate the state of a Compte (ACTIVE, INACTIVE, etc.)
    private boolean validateCompteState(Compte compte, EtatCompte etat, String numCompte) {
        if (!Objects.equals(compte.getEtatCompte(), etat)) {
            System.out.println("Le compte " + numCompte + " n'est pas actif");
            return true;
        }
        return false;
    }

    // Method to perform immediate execution of scheduled money transfers
    @Transactional
    @Override
    public void effectuerVirementPermanentAsync(VirementProgramme virementProgramme) {
        // Get client's and beneficiary's account details from the repository
        Compte clientCompte = compteRepository.getCompteByNumCompte(virementProgramme.getNumCompteClient());
        Compte beneficierCompte = compteRepository.getCompteByNumCompte(virementProgramme.getNumCompteBeneficier());

        // Validate the state of the client's account (must be ACTIVE)
        if (validateCompteState(clientCompte, EtatCompte.ACTIVE, virementProgramme.getNumCompteClient())) {
            notificationServiceVirement.clientCompteNotActive(virementProgramme);
            return;
        }

        // Validate the state of the beneficiary's account (must be ACTIVE)
        if (validateCompteState(beneficierCompte, EtatCompte.ACTIVE, virementProgramme.getNumCompteBeneficier())) {
            notificationServiceVirement.beneficierCompteNotActive(virementProgramme);
            return;
        }

        // Perform the money transfer between the accounts
        compteService.changeSolde(clientCompte.getNumCompte(), -virementProgramme.getMontant(), OPType.VIREMENT_PERMANENT);
        compteService.changeSolde(beneficierCompte.getNumCompte(), virementProgramme.getMontant(), OPType.VIREMENT_PERMANENT);

        // Save the transfer details in the virementPermanantRepository
        VirementPermanant virementPermanant=virementMapper.fromVirementProgrammeToVirementPermanent(virementProgramme);
        virementPermanant.setFrequence(virementProgramme.getFrequence());
        virementPermanant.setUser(clientCompte.getUser());

        Beneficier beneficier=beneficierRepository.getBeneficiersByNumCompte(beneficierCompte.getNumCompte());
        virementPermanant.setBeneficier(beneficier);

        virementPermanantRepository.save(virementPermanant);

        // Print success message
        System.out.println("Virement effectué avec succès");
    }

    // Scheduled method to execute pending scheduled money transfers
    @Scheduled(fixedRate = 5000) // Run every 5000 milliseconds (5 seconds)
    public void effectuerVirementProgramme()  {
        // Find pending scheduled transfers that are due for execution
        List<VirementProgramme> virementsProgramme = viremenProgrammeRepository.findPendingVirements(new Date());
        System.out.println("Les virements programme sont : " + virementsProgramme.size() + " virements");
        // Execute each pending scheduled transfer and update its status
        if (virementsProgramme.size() > 0) {
            for (VirementProgramme virementProgramme : virementsProgramme) {
                if (!virementProgramme.isEffectuer()) {
                    // Execute the pending scheduled transfer immediately
                    effectuerVirementPermanentAsync(virementProgramme);
                }

                // Mark the scheduled transfer as executed
                virementProgramme.setEffectuer(true);
                viremenProgrammeRepository.save(virementProgramme);
            }
        }

        System.out.println("Aucun virement programme n'a été effectué");
    }
}
