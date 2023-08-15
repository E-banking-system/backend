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
import org.springframework.data.repository.query.Param;
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
    final private OperationRepository operationRepository;

    @Override
    public Double getClientSolde(String userId){
        return compteRepository.getTotalSoldeByUserId(userId);
    }

    @Override
    public Date getLatestOperationByUserId(String userId){
        return compteRepository.getLatestOperationByUserId(userId);
    }

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

                log.info("Get account operations is done");
                return operationResDTO;
            });
        } catch (Exception e) {
            log.warn("This account is not exists");
            throw new CompteNotExistException("This account is not exists");
        }
    }

    @Override
    public Page<CompteResDTO> searchComptes(Pageable pageable, String keyword) {
        Page<Compte> comptePage = compteRepository.searchComptes(pageable, keyword);
        log.info("Search for accounts by keyword");
        return comptePage.map(compteMapper::fromCompteToCompteResDTO);
    }


    @Override
    @Transactional
    public void ajouterCompte(CompteReqDTO compteDTO) {
        Compte newCompte = compteMapper.fromCompteDTOToCompte(compteDTO);

        Optional<UserEntity> existingUserOptional = userRepository.findByEmail(compteDTO.getEmail());

        if (existingUserOptional.isPresent()) {
            compteRepository.save(newCompte);
            UserEntity existingUser = existingUserOptional.get();
            existingUser.addCompte(newCompte);
            userRepository.save(existingUser);

            // Send account info email to existing user
            emailSender.sendAccountInfosByEmail(existingUser, newCompte.getCodePIN());

            log.info("Account added to client that has this email : {}", compteDTO.getEmail());
        } else {
            log.warn("Client not found with this email : {}", compteDTO.getEmail());
        }
    }

    @Override
    public Page<CompteResDTO> getComptes(Pageable pageable) {
        Page<Compte> comptePage = compteRepository.findAll(pageable);
        log.info("Get accounts is done");
        return comptePage.map(compteMapper::fromCompteToCompteResDTO);
    }

    @Override
    public void activerCompte(String compteId) throws NotificationNotSended {
        Compte compte = compteRepository.getCompteById(compteId);
        if (compte != null) {
            compte.activerCompte();
            compteRepository.save(compte);
            notificationServiceVirement.sendActiverCompteNotificationToClient(compte.getId(),compte.getUser());
            log.info("Activate account is done");
        } else {
            log.warn("Account not found with the given ID");
            throw new IllegalArgumentException("Account not found with the given ID");
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
            log.info("Blocked account with ID: {}", compteId);
        } else {
            log.warn("Account is not found with the given ID");
            throw new IllegalArgumentException("Account is not found with the given ID");
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
            log.info("Suspended account with ID: {}", compteId);
        } else {
            throw new IllegalArgumentException("Account not found with the given ID");
        }
    }

    @Override
    public Page<CompteResDTO> getClientComptes(String userId, Pageable pageable, String keyword) {
        if (keyword == null) {
            keyword = "";
        }
        Page<Compte> comptePage = compteRepository.searchComptesByUserIdAndKeyword(userId, keyword, pageable);
        log.info("Get client accounts is done");
        return comptePage.map(compteMapper::fromCompteToCompteResDTO);
    }

    @Override
    public void demandeSuspendCompte(DemandeSuspendDTO demandeSuspendDTO) throws NotificationNotSended {
        UserEntity user=userRepository.findByRole(ERole.BANQUIER).get(0);
        notificationServiceVirement.sendDemandeSuspendCompteNotificationToBanquier(demandeSuspendDTO.getCompteId(),user);
        log.info("Sent request to suspend an account that has this ID : "+demandeSuspendDTO.getCompteId());
    }

    @Override
    public void demandeBlockCompte(DemandeBlockDTO demandeBlockDTO) throws NotificationNotSended {
        UserEntity user=userRepository.findByRole(ERole.BANQUIER).get(0);
        notificationServiceVirement.sendDemandeBlockCompteNotificationToBanquier(demandeBlockDTO.getCompteId(),user);
        log.info("Sent request to block an account that has this ID : "+demandeBlockDTO.getCompteId());
    }

    @Override
    public void demandeActivateCompte(DemandeActivateDTO demandeActivateDTO) throws NotificationNotSended {
        UserEntity user=userRepository.findByRole(ERole.BANQUIER).get(0);
        notificationServiceVirement.sendDemandeActivateCompteNotificationToBanquier(demandeActivateDTO.getCompteId(), user);
        log.info("Sent request to activate an account that has this ID : "+demandeActivateDTO.getCompteId());
    }
}
