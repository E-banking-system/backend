package adria.sid.ebanckingbackend.services.operation.changeSolde;

import adria.sid.ebanckingbackend.dtos.operation.DepotReqDTO;
import adria.sid.ebanckingbackend.dtos.operation.RetraitReqDTO;
import adria.sid.ebanckingbackend.ennumerations.EtatCompte;
import adria.sid.ebanckingbackend.entities.Compte;
import adria.sid.ebanckingbackend.exceptions.CompteNotActiveException;
import adria.sid.ebanckingbackend.exceptions.InsufficientBalanceException;
import adria.sid.ebanckingbackend.exceptions.NotificationNotSended;
import adria.sid.ebanckingbackend.repositories.CompteRepository;
import adria.sid.ebanckingbackend.services.notification.OperationNotificationService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChangeSoldeServiceImpl implements ChangeSoldeService {
    final private CompteRepository compteRepository;
    final private OperationNotificationService operationNotificationService;

    @Transactional
    @Override
    public void depot(DepotReqDTO depotReqDTO) throws NotificationNotSended {
        Compte compte=compteRepository.getCompteByNumCompte(depotReqDTO.getNumCompte());
        if(compte != null){
            if(!compte.getEtatCompte().equals(EtatCompte.ACTIVE)){
                throw new CompteNotActiveException("This account is not active.");
            }

            double newSolde=compte.getSolde()+depotReqDTO.getMontant();
            compte.setSolde(newSolde);
            compteRepository.save(compte);
            operationNotificationService.sendDepotNotificationToClient(compte,depotReqDTO.getMontant());
        } else {
            throw new IllegalArgumentException("This account is not found with the given ID");
        }
    }

    @Transactional
    @Override
    public void retrait(RetraitReqDTO retraitReqDTO) throws InsufficientBalanceException, NotificationNotSended {
        Compte compte=compteRepository.getCompteByNumCompte(retraitReqDTO.getNumCompte());
        if(compte != null){
            if(!compte.getEtatCompte().equals(EtatCompte.ACTIVE)){
                throw new CompteNotActiveException("This account is not active.");
            }

            double newSolde=compte.getSolde()-retraitReqDTO.getMontant();

            if(newSolde>=0){
                compte.setSolde(newSolde);
                compteRepository.save(compte);
                operationNotificationService.sendRetraitNotificationToClient(compte, retraitReqDTO.getMontant());
            } else{
                throw new InsufficientBalanceException("Insufficient balance.");
            }

        } else {
            throw new IllegalArgumentException("This account is not found with the given ID");
        }
    }
}
