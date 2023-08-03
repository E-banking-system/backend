package adria.sid.ebanckingbackend.services.virement;

import adria.sid.ebanckingbackend.dtos.virement.VirementPermanentReqDTO;
import adria.sid.ebanckingbackend.dtos.virement.VirementUnitReqDTO;
import adria.sid.ebanckingbackend.ennumerations.EVType;
import adria.sid.ebanckingbackend.ennumerations.EtatCompte;
import adria.sid.ebanckingbackend.entities.*;
import adria.sid.ebanckingbackend.exceptions.*;
import adria.sid.ebanckingbackend.mappers.VirementMapper;
import adria.sid.ebanckingbackend.repositories.*;
import adria.sid.ebanckingbackend.services.compte.CompteService;
import adria.sid.ebanckingbackend.services.notification.NotificationService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
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
    final private NotificationService notificationService;

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
        if (!validateCompteState(clientCompte, EtatCompte.ACTIVE, viremenentReqDTO.getNumCompteClient())) {
            return;
        }

        // Validate the state of the beneficiary's account (must be ACTIVE)
        if (!validateCompteState(beneficierCompte, EtatCompte.ACTIVE, viremenentReqDTO.getNumCompteBeneficier())) {
            return;
        }

        // Deduct the amount from the client's account and add it to the beneficiary's account
        compteService.changeSolde(clientCompte.getNumCompte(), -viremenentReqDTO.getMontant(), true);
        compteService.changeSolde(beneficierCompte.getNumCompte(), viremenentReqDTO.getMontant(), true);

        // Save the transfer details in the virementUnitaireRepository
        VirementUnitaire virementUnitaire = virementMapper.fromVirementReqDTOToVirementUnitaire(viremenentReqDTO);
        virementUnitaireRepository.save(virementUnitaire);

        // Create a notification for the beneficiary
        Notification notification = new Notification();
        notification.setId(UUID.randomUUID().toString());
        notification.setContenu("Un virement effectué avec succès par monsieur/madame : " +
                clientCompte.getUser().getNom() + clientCompte.getUser().getPrenom());
        notification.setUser(beneficierCompte.getUser());
        notification.setDateEnvoie(new Date());
        notification.setTitre("Un virement de " + virementUnitaire.getMontant() + " DH effectué avec succès");
        notificationService.saveNotification(notification);

        // Log the notification ID
        log.info("Sent transfer notification with ID: {}", notification.getId());

        // Print success message
        System.out.println("Virement effectué avec succès");
    }

    // Method to perform a scheduled money transfer (Virement Permanent)
    @Override
    public void effectuerVirementPermanent(VirementPermanentReqDTO virementPermanentReqDTO) throws DatesVirementPermanentAreNotValide {
        // Validate the execution dates for the scheduled transfer
        if (virementPermanentReqDTO.getPremierDateExecution().compareTo(virementPermanentReqDTO.getDateFinExecution()) > 0) {
            throw new DatesVirementPermanentAreNotValide("Les dates pour programmé ce virement ne sont pas valide");
        }

        // Convert the scheduled transfer details to VirementProgramme
        VirementProgramme virementProgramme = virementMapper.fromVirementPermanentReqDTOToVirementProgramme(virementPermanentReqDTO);

        // Generate the list of execution dates for the scheduled transfer
        List<Date> datesExecution = virementProgramme.genererDatesExecution();

        // Add the execution dates to the database as VirementProgramme entries
        for (Date dateExecution : datesExecution) {
            VirementProgramme virementProgrammeExecution = new VirementProgramme();
            virementProgrammeExecution.setId(UUID.randomUUID().toString());
            virementProgrammeExecution.setPremierDateExecution(dateExecution);
            virementProgrammeExecution.setDateFinExecution(virementProgramme.getDateFinExecution());
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
            return false;
        }
        return true;
    }

    // Method to perform immediate execution of scheduled money transfers
    @Transactional
    @Override
    public void effectuerVirementPermanentNow(VirementUnitReqDTO viremenentReqDTO) {
        // Get client's and beneficiary's account details from the repository
        Compte clientCompte = compteRepository.getCompteByNumCompte(viremenentReqDTO.getNumCompteClient());
        Compte beneficierCompte = compteRepository.getCompteByNumCompte(viremenentReqDTO.getNumCompteBeneficier());

        // Validate the existence of the client's and beneficiary's accounts
        if (!validateCompteExistence(clientCompte, viremenentReqDTO.getNumCompteClient())) {
            return;
        }

        if (!validateCompteExistence(beneficierCompte, viremenentReqDTO.getNumCompteBeneficier())) {
            return;
        }

        // Validate the state of the client's account (must be ACTIVE)
        if (!validateCompteState(clientCompte, EtatCompte.ACTIVE, viremenentReqDTO.getNumCompteClient())) {
            return;
        }

        // Validate the state of the beneficiary's account (must be ACTIVE)
        if (!validateCompteState(beneficierCompte, EtatCompte.ACTIVE, viremenentReqDTO.getNumCompteBeneficier())) {
            return;
        }

        // Perform the money transfer between the accounts
        compteService.changeSolde(clientCompte.getNumCompte(), -viremenentReqDTO.getMontant(), true);
        compteService.changeSolde(beneficierCompte.getNumCompte(), viremenentReqDTO.getMontant(), true);

        // Save the transfer details in the virementPermanantRepository
        VirementPermanant virementPermanant = virementMapper.fromVirementReqDTOToVirementPermanent(viremenentReqDTO);
        virementPermanantRepository.save(virementPermanant);

        // Print success message
        System.out.println("Virement effectué avec succès");
    }

    // Scheduled method to execute pending scheduled money transfers
    @Bean
    @Scheduled(fixedRate = 5000) // Run every 5000 milliseconds (5 seconds)
    public void effectuerVirementProgramme()  {
        // Find pending scheduled transfers that are due for execution
        List<VirementProgramme> virementsProgramme = viremenProgrammeRepository.findPendingVirements(new Date());
        System.out.println("Les virements programme sont : " + virementsProgramme.size() + " virements");

        // Execute each pending scheduled transfer and update its status
        if (virementsProgramme.size() > 0) {
            for (VirementProgramme virementProgramme : virementsProgramme) {
                if (!virementProgramme.isEffectuer()) {
                    VirementUnitReqDTO virementUnitReqDTO = new VirementUnitReqDTO();
                    virementUnitReqDTO.setMontant(virementProgramme.getMontant());
                    virementUnitReqDTO.setNumCompteBeneficier(virementProgramme.getNumCompteBeneficier());
                    virementUnitReqDTO.setNumCompteClient(virementProgramme.getNumCompteClient());

                    // Execute the pending scheduled transfer immediately
                    effectuerVirementPermanentNow(virementUnitReqDTO);
                }

                // Mark the scheduled transfer as executed
                virementProgramme.setEffectuer(true);
                viremenProgrammeRepository.save(virementProgramme);
            }
        }

        System.out.println("Aucun virement programme n'a été effectué");
    }
}
