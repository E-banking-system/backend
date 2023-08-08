package adria.sid.ebanckingbackend.services.operation.changeSolde;

import adria.sid.ebanckingbackend.dtos.operation.DepotReqDTO;
import adria.sid.ebanckingbackend.dtos.operation.RetraitReqDTO;
import adria.sid.ebanckingbackend.exceptions.InsufficientBalanceException;
import adria.sid.ebanckingbackend.exceptions.NotificationNotSended;
import adria.sid.ebanckingbackend.exceptions.OperationNotSaved;

public interface ChangeSoldeService {
    void depot(DepotReqDTO depotReqDTO) throws NotificationNotSended, OperationNotSaved;
    void retrait(RetraitReqDTO retraitReqDTO) throws InsufficientBalanceException, NotificationNotSended, OperationNotSaved;
}
