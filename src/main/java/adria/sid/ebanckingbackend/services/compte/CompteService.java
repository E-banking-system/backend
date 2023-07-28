package adria.sid.ebanckingbackend.services.compte;

import adria.sid.ebanckingbackend.dtos.ReqCreateAccountDTO;
import org.springframework.stereotype.Service;

@Service
public interface CompteService {
    void createAccountForExistingUserAndSendEmail(ReqCreateAccountDTO accountDTO);
}
