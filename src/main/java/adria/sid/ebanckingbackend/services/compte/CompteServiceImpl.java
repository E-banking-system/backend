package adria.sid.ebanckingbackend.services.compte;

import adria.sid.ebanckingbackend.dtos.compte.*;
import adria.sid.ebanckingbackend.dtos.operation.OperationResDTO;
import adria.sid.ebanckingbackend.ennumerations.ERole;
import adria.sid.ebanckingbackend.entities.*;
import adria.sid.ebanckingbackend.exceptions.CompteNotExistException;
import adria.sid.ebanckingbackend.exceptions.NotificationNotSended;
import adria.sid.ebanckingbackend.mappers.CompteMapper;
import adria.sid.ebanckingbackend.mappers.OperationMapper;
import adria.sid.ebanckingbackend.repositories.CompteRepository;
import adria.sid.ebanckingbackend.repositories.OperationRepository;
import adria.sid.ebanckingbackend.repositories.UserRepository;
import adria.sid.ebanckingbackend.repositories.VirementUnitaireRepository;
import adria.sid.ebanckingbackend.services.email.EmailSender;
import adria.sid.ebanckingbackend.services.notification.OperationNotificationService;
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
    final private OperationNotificationService notificationServiceVirement;
    final private EmailSender emailSender;
    final private CompteMapper compteMapper;
    final private OperationMapper operationMapper;
    final private VirementUnitaireRepository virementUnitaireRepository;
    final private OperationRepository operationRepository;

    @Override
    public Page<OperationResDTO> getCompteOperations(Pageable pageable, String compteId, String userId) throws CompteNotExistException {
        try {
            Page<Operation> operationPage = operationRepository.findByCompteId(pageable, compteId, userId);
            return operationPage.map(operation -> {
                OperationResDTO operationResDTO = operationMapper.fromOperationToOperationResDTO(operation);
                operationResDTO.setCompteId(operation.getCompte().getId());

                if (operation instanceof Virement) {
                    Virement virement = (Virement) operation;
                    operationResDTO.setBeneficierId(virement.getBeneficier().getBeneficier_id());
                }

                return operationResDTO;
            });
        } catch (Exception e) {
            throw new CompteNotExistException("This account with this id is not exists");
        }
    }

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
    public void activerCompte(String compteId) throws NotificationNotSended {
        Compte compte = compteRepository.getCompteById(compteId);
        if (compte != null) {
            compte.activerCompte();
            compteRepository.save(compte);
            notificationServiceVirement.sendActiverCompteNotificationToClient(compte.getId(),compte.getUser());
        } else {
            throw new IllegalArgumentException("Compte not found with the given ID");
        }
    }

    @Override
    public void blockCompte(String compteId) throws NotificationNotSended {
        Compte compte = compteRepository.getCompteById(compteId);
        if (compte != null) {
            compte.setDerniereDateBloquage(new Date());
            compte.blockerCompte();
            compteRepository.save(compte);
            notificationServiceVirement.sendBlockeCompteNotificationToClient(compte.getId(),compte.getUser());
            log.info("Blocked compte with ID: {}", compteId);
        } else {
            throw new IllegalArgumentException("Compte not found with the given ID");
        }
    }

    @Override
    public void suspendCompte(String compteId) throws NotificationNotSended {
        Compte compte = compteRepository.getCompteById(compteId);
        if (compte != null) {
            compte.setDerniereDateSuspention(new Date());
            compte.suspenduCompte();
            compteRepository.save(compte);
            notificationServiceVirement.sendSuspendCompteNotificationToClient(compte.getId(),compte.getUser());
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
    public void demandeSuspendCompte(DemandeSuspendDTO demandeSuspendDTO) throws NotificationNotSended {
        UserEntity user=userRepository.findByRole(ERole.BANQUIER).get(0);
        notificationServiceVirement.sendDemandeSuspendCompteNotificationToBanquier(demandeSuspendDTO.getCompteId(),user);
        log.info("Sent demande de suspend d'un compte numéro : "+demandeSuspendDTO.getCompteId());
    }

    @Override
    public void demandeBlockCompte(DemandeBlockDTO demandeBlockDTO) throws NotificationNotSended {
        UserEntity user=userRepository.findByRole(ERole.BANQUIER).get(0);
        notificationServiceVirement.sendDemandeBlockCompteNotificationToBanquier(demandeBlockDTO.getCompteId(),user);
        log.info("Sent demande de block d'un compte numéro : "+demandeBlockDTO.getCompteId());
    }

    @Override
    public void demandeActivateCompte(DemandeActivateDTO demandeActivateDTO) throws NotificationNotSended {
        UserEntity user=userRepository.findByRole(ERole.BANQUIER).get(0);
        notificationServiceVirement.sendDemandeActivateCompteNotificationToBanquier(demandeActivateDTO.getCompteId(), user);
        log.info("Sent demande d'activer d'un compte numéro : "+demandeActivateDTO.getCompteId());
    }
}
