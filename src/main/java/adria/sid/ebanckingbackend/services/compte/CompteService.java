package adria.sid.ebanckingbackend.services.compte;

import adria.sid.ebanckingbackend.dtos.compte.*;
import adria.sid.ebanckingbackend.entities.Compte;
import adria.sid.ebanckingbackend.entities.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CompteService {
    void ajouterCompte(CompteReqDTO accountDTO);
    Page<CompteResDTO> getComptes(Pageable pageable);
    void activerCompte(String compteId);
    void blockCompte(String compteId);
    void suspendCompte(String compteId);
    void changeSolde(String compteId, Double montant);
    List<CompteResDTO> getClientComptes(String userId);
    Notification demandeSuspendCompte(DemandeSuspendDTO demandeSuspendDTO);
    Notification demandeActivateCompte(DemandeActivateDTO demandeActivateDTO);
    Notification demandeBlockCompte(DemandeBlockDTO demandeBlockDTO);
}
