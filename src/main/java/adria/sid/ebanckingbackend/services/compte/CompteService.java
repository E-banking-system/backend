package adria.sid.ebanckingbackend.services.compte;

import adria.sid.ebanckingbackend.dtos.compte.CompteReqDTO;
import adria.sid.ebanckingbackend.dtos.compte.CompteResDTO;
import adria.sid.ebanckingbackend.entities.Compte;
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
}
