package adria.sid.ebanckingbackend.services.operation.changeSolde;

import adria.sid.ebanckingbackend.dtos.operation.DepotReqDTO;
import adria.sid.ebanckingbackend.dtos.operation.RetraitReqDTO;
import adria.sid.ebanckingbackend.ennumerations.EtatCompte;
import adria.sid.ebanckingbackend.entities.*;
import adria.sid.ebanckingbackend.exceptions.CompteNotActiveException;
import adria.sid.ebanckingbackend.exceptions.InsufficientBalanceException;
import adria.sid.ebanckingbackend.exceptions.NotificationNotSended;
import adria.sid.ebanckingbackend.exceptions.OperationNotSaved;
import adria.sid.ebanckingbackend.repositories.CompteRepository;
import adria.sid.ebanckingbackend.repositories.DepotRepository;
import adria.sid.ebanckingbackend.repositories.RetraitRepository;
import adria.sid.ebanckingbackend.services.notification.OperationNotificationService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class ChangeSoldeServiceImpl implements ChangeSoldeService {
    final private CompteRepository compteRepository;
    final private OperationNotificationService operationNotificationService;
    final private DepotRepository depotRepository;
    final private RetraitRepository retraitRepository;

    @Transactional
    @Override
    public void depot(DepotReqDTO depotReqDTO) throws NotificationNotSended, OperationNotSaved {
        Compte compte=compteRepository.getCompteByNumCompte(depotReqDTO.getNumCompte());
        if(compte != null){
            if(!compte.getEtatCompte().equals(EtatCompte.ACTIVE)){
                log.warn("This account is not active");
                throw new CompteNotActiveException("This account is not active");
            }

            double newSolde=compte.getSolde()+depotReqDTO.getMontant();
            compte.setSolde(newSolde);
            compteRepository.save(compte);
            operationNotificationService.sendDepotNotificationToClient(compte,depotReqDTO.getMontant());

            try {
                Depot depot=new Depot();
                depot.setId(UUID.randomUUID().toString());
                depot.setMontant(depotReqDTO.getMontant());
                depot.setDateOperation(new Date());
                depot.setCompte(compte);

                depotRepository.save(depot);
                log.info("Depot is saved with success");
            } catch (Exception e){
                log.warn("This depot is not saved");
                throw new OperationNotSaved("This depot is not saved");
            }

        } else {
            log.warn("This account is not found");
            throw new IllegalArgumentException("This account is not found");
        }
    }

    @Transactional
    @Override
    public void retrait(RetraitReqDTO retraitReqDTO) throws InsufficientBalanceException, NotificationNotSended, OperationNotSaved {
        Compte compte=compteRepository.getCompteByNumCompte(retraitReqDTO.getNumCompte());
        if(compte != null){
            if(!compte.getEtatCompte().equals(EtatCompte.ACTIVE)){
                log.warn("This account is not active");
                throw new CompteNotActiveException("This account is not active");
            }

            double newSolde=compte.getSolde()+retraitReqDTO.getMontant();

            if(newSolde>=0){
                compte.setSolde(newSolde);
                compteRepository.save(compte);
                operationNotificationService.sendRetraitNotificationToClient(compte, retraitReqDTO.getMontant());

                try {
                    Retrait retrait=new Retrait();
                    retrait.setId(UUID.randomUUID().toString());
                    retrait.setMontant(retraitReqDTO.getMontant());
                    retrait.setDateOperation(new Date());
                    retrait.setCompte(compte);

                    retraitRepository.save(retrait);
                    log.info("retract is saved with success");
                } catch (Exception e){
                    log.warn("This retract is not saved");
                    throw new OperationNotSaved("This retract is not saved");
                }
            } else{
                log.warn("Insufficient balance");
                throw new InsufficientBalanceException("Insufficient balance");
            }

        } else {
            log.warn("This account is not found");
            throw new IllegalArgumentException("This account is not found");
        }
    }
}
