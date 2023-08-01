package adria.sid.ebanckingbackend.services.compte;

import adria.sid.ebanckingbackend.dtos.compte.*;
import adria.sid.ebanckingbackend.ennumerations.ERole;
import adria.sid.ebanckingbackend.entities.Compte;
import adria.sid.ebanckingbackend.entities.Notification;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.exceptions.IdUserIsNotValideException;
import adria.sid.ebanckingbackend.mappers.CompteMapper;
import adria.sid.ebanckingbackend.repositories.CompteRepository;
import adria.sid.ebanckingbackend.repositories.UserRepository;
import adria.sid.ebanckingbackend.services.email.EmailSender;
import adria.sid.ebanckingbackend.services.notification.NotificationService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class CompteServiceImpl implements CompteService {

    final private CompteRepository compteRepository;
    final private UserRepository userRepository;
    final private NotificationService notificationService;
    final private EmailSender emailSender;
    final private CompteMapper compteMapper;

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

            Notification notification=new Notification();
            notification.setId(UUID.randomUUID().toString());
            notification.setContenu("Id compte : "+compte.getId());
            notification.setUser(compte.getUser());
            notification.setDateEnvoie(new Date());
            notification.setTitre("Compte activer avec success");
            notificationService.saveNotification(notification);
            log.info("Activated compte with ID: {}", compteId);
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

            Notification notification=new Notification();
            notification.setId(UUID.randomUUID().toString());
            notification.setContenu("Id compte : "+compte.getId());
            notification.setUser(compte.getUser());
            notification.setDateEnvoie(new Date());
            notification.setTitre("Compte blocké avec success");
            notificationService.saveNotification(notification);
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

            Notification notification=new Notification();
            notification.setId(UUID.randomUUID().toString());
            notification.setContenu("Id compte : "+compte.getId());
            notification.setUser(compte.getUser());
            notification.setDateEnvoie(new Date());
            notification.setTitre("Compte suspendé avec success");
            notificationService.saveNotification(notification);
            log.info("Suspended compte with ID: {}", compteId);
        } else {
            throw new IllegalArgumentException("Compte not found with the given ID");
        }
    }

    @Override
    @Transactional
    public void changeSolde(String compteId, Double montant) {
        Compte compte = compteRepository.getCompteById(compteId);
        if (compte != null) {
            double newSolde = compte.getSolde() + montant;
            if (newSolde >= 0) {
                compte.setSolde(newSolde);
                compteRepository.save(compte);

                log.info("Changed solde for compte with ID: {} by amount: {}", compteId, montant);
            } else {
                throw new IllegalArgumentException("Insufficient balance.");
            }
        } else {
            throw new IllegalArgumentException("Compte not found with the given ID");
        }
    }

    @Override
    public List<CompteResDTO> getClientComptes(String userId) {
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            List<Compte> compteList = compteRepository.getComptesByUserId(userId);
            return compteMapper.toComptesResDTOs(compteList);
        } else {
            throw new IdUserIsNotValideException("Id user is not valid");
        }
    }

    @Override
    public Notification demandeSuspendCompte(DemandeSuspendDTO demandeSuspendDTO){
        UserEntity user=userRepository.findByRole(ERole.BANQUIER).get(0);
        Notification notification=new Notification();
        notification.setId(UUID.randomUUID().toString());
        notification.setContenu("Id compte : "+demandeSuspendDTO.getCompteId());
        notification.setUser(user);
        notification.setDateEnvoie(new Date());
        notification.setTitre("Demande de suspend d'un compte");
        notificationService.saveNotification(notification);

        log.info("Sent demande de suspend d'un compte notification with ID: {}", notification.getId());
        return notification;
    }

    @Override
    public Notification demandeBlockCompte(DemandeBlockDTO demandeBlockDTO){
        UserEntity user=userRepository.findByRole(ERole.BANQUIER).get(0);
        Notification notification=new Notification();
        notification.setId(UUID.randomUUID().toString());
        notification.setContenu("Id compte : "+demandeBlockDTO.getCompteId());
        notification.setUser(user);
        notification.setDateEnvoie(new Date());
        notification.setTitre("Demande de block d'un compte");
        notificationService.saveNotification(notification);

        log.info("Sent demande de block d'un compte notification with ID: {}", notification.getId());
        return notification;
    }

    @Override
    public Notification demandeActivateCompte(DemandeActivateDTO demandeActivateDTO){
        UserEntity user=userRepository.findByRole(ERole.BANQUIER).get(0);
        Notification notification=new Notification();
        notification.setId(UUID.randomUUID().toString());
        notification.setContenu("Id compte : "+demandeActivateDTO.getCompteId());
        notification.setUser(user);
        notification.setDateEnvoie(new Date());
        notification.setTitre("Demande d'activer d'un compte");
        notificationService.saveNotification(notification);

        log.info("Sent demande d'activer d'un compte notification with ID: {}", notification.getId());
        return notification;
    }
}
