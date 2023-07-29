package adria.sid.ebanckingbackend.services.compte;

import adria.sid.ebanckingbackend.dtos.compte.CompteReqDTO;
import adria.sid.ebanckingbackend.dtos.compte.CompteResDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface CompteService {
    void ajouterCompte(CompteReqDTO accountDTO);
    Page<CompteResDTO> getAccounts(Pageable pageable);
    void activerCompte(String compteId);
    void blockCompte(String compteId);
    void suspendCompte(String compteId);
}
