package adria.sid.ebanckingbackend.services.compte;

import adria.sid.ebanckingbackend.dtos.CompteReqDTO;
import adria.sid.ebanckingbackend.dtos.CompteResDTO;
import adria.sid.ebanckingbackend.entities.Compte;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CompteService {
    void createAccountForExistingUserAndSendEmail(CompteReqDTO accountDTO);
    Page<CompteResDTO> getAccounts(Pageable pageable);
}
