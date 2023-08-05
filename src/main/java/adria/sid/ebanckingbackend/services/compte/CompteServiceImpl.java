package adria.sid.ebanckingbackend.services.compte;

import adria.sid.ebanckingbackend.dtos.compte.*;
import adria.sid.ebanckingbackend.ennumerations.ERole;
import adria.sid.ebanckingbackend.ennumerations.EtatCompte;
import adria.sid.ebanckingbackend.entities.Compte;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.exceptions.CompteNotActiveException;
import adria.sid.ebanckingbackend.mappers.CompteMapper;
import adria.sid.ebanckingbackend.repositories.CompteRepository;
import adria.sid.ebanckingbackend.repositories.UserRepository;
import adria.sid.ebanckingbackend.services.email.EmailSender;
import adria.sid.ebanckingbackend.services.notification.NotificationService;
import adria.sid.ebanckingbackend.services.notification.NotificationCompteService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CompteServiceImpl implements CompteService {

    final private CompteRepository compteRepository;
    final private UserRepository userRepository;
    final private NotificationService notificationService;
    final private NotificationCompteService notificationServiceVirement;
    final private EmailSender emailSender;
    final private CompteMapper compteMapper;

    @Override
    public Page<CompteResDTO> searchComptes(Pageable pageable, String keyword) {
        Page<Compte> comptePage = compteRepository.searchComptes(pageable, keyword);
        return comptePage.map(compteMapper::fromCompteToCompteResDTO);
    }


    @Override
    @Transactional
    public void ajouterCompte(CompteReqDTO compteDTO) {
        if (compteDTO == null || compteDTO.getEmail() == null) {
            throw new IllegalArgumentException("Invalid compte");
        }

        Compte newCompte = compteMapper.fromCompteDTOToCompte(compteDTO);

        Optional<UserEntity> existingUserOptional = userRepository.findByEmail(compteDTO.getEmail());

        if (existingUserOptional.isPresent()) {
            compteRepository.save(newCompte);
            UserEntity existingUser = existingUserOptional.get();
            existingUser.addCompte(newCompte);
            userRepository.save(existingUser);

            // Send account info email to existing user
            emailSender.sendAccountInfosByEmail(existingUser, newCompte.getCodePIN());

            log.info("Added compte for user with email: {}", compteDTO.getEmail());
        } else {
            log.warn("User with email {} not found.", compteDTO.getEmail());
        }
    }

    @Override
    public Page<CompteResDTO> getComptes(Pageable pageable) {
        Page<Compte> comptePage = compteRepository.findAll(pageable);
        return comptePage.map(compteMapper::fromCompteToCompteResDTO);
    }

    @Override
    public void activerCompte(String compteId) {
        Compte compte = compteRepository.getCompteById(compteId);
        if (compte != null) {
            compte.activerCompte();
            compteRepository.save(compte);
            notificationServiceVirement.activerCompte(compte.getId(),compte.getUser());
        } else {
            throw new IllegalArgumentException("Compte not found with the given ID");
        }
    }

    @Override
    public void blockCompte(String compteId) {
        Compte compte = compteRepository.getCompteById(compteId);
        if (compte != null) {
            compte.setDerniereDateBloquage(new Date());
            compte.blockerCompte();
            compteRepository.save(compte);
            notificationServiceVirement.blockeCompte(compte.getId(),compte.getUser());
            log.info("Blocked compte with ID: {}", compteId);
        } else {
            throw new IllegalArgumentException("Compte not found with the given ID");
        }
    }

    @Override
    public void suspendCompte(String compteId) {
        Compte compte = compteRepository.getCompteById(compteId);
        if (compte != null) {
            compte.setDerniereDateSuspention(new Date());
            compte.suspenduCompte();
            compteRepository.save(compte);
            notificationServiceVirement.suspendCompte(compte.getId(),compte.getUser());
            log.info("Suspended compte with ID: {}", compteId);
        } else {
            throw new IllegalArgumentException("Compte not found with the given ID");
        }
    }

    @Override
    public Page<CompteResDTO> getClientComptes(String userId, Pageable pageable, String keyword) {
        if (keyword == null) {
            keyword = "";
        }
        Page<Compte> comptePage = compteRepository.searchComptesByUserIdAndKeyword(userId, keyword, pageable);
        return comptePage.map(compteMapper::fromCompteToCompteResDTO);
    }

    @Override
    public void demandeSuspendCompte(DemandeSuspendDTO demandeSuspendDTO){
        UserEntity user=userRepository.findByRole(ERole.BANQUIER).get(0);
        notificationServiceVirement.demandeSuspendCompte(demandeSuspendDTO.getCompteId(),user);
        log.info("Sent demande de suspend d'un compte numéro : "+demandeSuspendDTO.getCompteId());
    }

    @Override
    public void demandeBlockCompte(DemandeBlockDTO demandeBlockDTO){
        UserEntity user=userRepository.findByRole(ERole.BANQUIER).get(0);
        notificationServiceVirement.demandeBlockCompte(demandeBlockDTO.getCompteId(),user);
        log.info("Sent demande de block d'un compte numéro : "+demandeBlockDTO.getCompteId());
    }

    @Override
    public void demandeActivateCompte(DemandeActivateDTO demandeActivateDTO){
        UserEntity user=userRepository.findByRole(ERole.BANQUIER).get(0);
        notificationServiceVirement.demandeActivateCompte(demandeActivateDTO.getCompteId(), user);
        log.info("Sent demande d'activer d'un compte numéro : "+demandeActivateDTO.getCompteId());
    }

    @Transactional
    public Boolean credit(String numCompte,Double montant,Boolean isVirement){
        Compte compte = compteRepository.getCompteByNumCompte(numCompte);
        if (compte != null) {
            if(!compte.getEtatCompte().equals(EtatCompte.ACTIVE)){
                throw new CompteNotActiveException("Ce compte n'est pas active.");
            }

            double newSolde = compte.getSolde() - montant;
            if (newSolde >= 0) {
                compte.setSolde(newSolde);
                compteRepository.save(compte);

                log.info("Changed solde for compte  NumCompte: {} by amount: {}", numCompte, montant);

                if(!isVirement) {
                    notificationServiceVirement.retraitCompte(numCompte,compte.getUser(),montant,newSolde);
                    log.info("Retrait de "+montant);
                }
                return true;
            } else {
                if(isVirement){
                    notificationServiceVirement.soldeInsifisantCompte(numCompte,compte.getUser());
                    log.info("Insufficient balance.");
                } else{
                    throw new IllegalArgumentException("Insufficient balance.");
                }
            }
        } else {
            throw new IllegalArgumentException("Compte not found with the given ID");
        }
        return false;
    }

    @Transactional
    public Boolean debit(String numCompte,Double montant,Boolean isVirement){
        Compte compte = compteRepository.getCompteByNumCompte(numCompte);
        if (compte != null) {
            if(!compte.getEtatCompte().equals(EtatCompte.ACTIVE)){
                throw new CompteNotActiveException("Ce compte n'est pas active.");
            }

            double newSolde = compte.getSolde() + montant;
            compte.setSolde(newSolde);
            compteRepository.save(compte);

            log.info("Changed solde for compte  NumCompte: {} by amount: {}", numCompte, montant);
            if(!isVirement) {
                notificationServiceVirement.depotCompte(numCompte,compte.getUser(),montant,newSolde);
                log.info("Depot de "+montant);
            }
            return true;
        } else {
            throw new IllegalArgumentException("Compte not found with the given ID");
        }
    }

    @Override
    @Transactional
    public void changeSolde(String numCompte, Double montant,Boolean isVirement) {
        Boolean success;
        if (montant > 0) {
            success = debit(numCompte, montant,isVirement);
            if (success) {
                log.info("Depot effectué avec succès : {}", montant);
            }
        } else {
            success = credit(numCompte, -montant,isVirement);
            if (success) {
                log.info("Retrait effectué avec succès : {}", -montant);
            }
        }
        if(isVirement && success){
            // Create a notification for the client
            notificationServiceVirement.virementToClientCompte(numCompte,montant);
            log.info("Virement permanent effectue");
        }
    }
}
