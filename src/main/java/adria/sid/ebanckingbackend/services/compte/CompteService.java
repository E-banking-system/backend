package adria.sid.ebanckingbackend.services.compte;

import adria.sid.ebanckingbackend.dtos.CompteDTO;
import org.springframework.stereotype.Service;

@Service
public interface CompteService {
    void createAccountForExistingUserAndSendEmail(CompteDTO accountDTO);
}
