package adria.sid.ebanckingbackend.services.compte;

import adria.sid.ebanckingbackend.dtos.compte.*;
import adria.sid.ebanckingbackend.dtos.operation.OperationResDTO;
import adria.sid.ebanckingbackend.dtos.operation.OperationsCountByTimeDTO;
import adria.sid.ebanckingbackend.exceptions.CompteNotExistException;
import adria.sid.ebanckingbackend.exceptions.NotificationNotSended;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface CompteService {
    List<OperationsCountByTimeDTO> getAllOperationsCountByTime();
    Date getLatestOperation();
    Long getCountActiveAccount();
    void ajouterCompte(CompteReqDTO accountDTO);
    Page<CompteResDTO> getComptes(Pageable pageable);
    void activerCompte(String compteId) throws NotificationNotSended;
    void blockCompte(String compteId) throws NotificationNotSended;
    void suspendCompte(String compteId) throws NotificationNotSended;
    void demandeSuspendCompte(DemandeSuspendDTO demandeSuspendDTO) throws NotificationNotSended;
    void demandeActivateCompte(DemandeActivateDTO demandeActivateDTO) throws NotificationNotSended;
    void demandeBlockCompte(DemandeBlockDTO demandeBlockDTO) throws NotificationNotSended;

    Page<OperationResDTO> getCompteOperations(Pageable pageable, String compteId, String userId) throws CompteNotExistException;

    Page<CompteResDTO> searchComptes(Pageable pageable, String keyword);
    Page<CompteResDTO> getClientComptes(String userId, Pageable pageable, String keyword);

    double getClientSolde(String userId);
    Date getLatestOperationByUserId(String userId);
    List<OperationsCountByTimeDTO> getOperationsCountByTime(String userId);
}
