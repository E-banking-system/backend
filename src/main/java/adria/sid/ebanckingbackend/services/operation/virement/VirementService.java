package adria.sid.ebanckingbackend.services.operation.virement;

import adria.sid.ebanckingbackend.dtos.operation.VirementPermaReqDTO;
import adria.sid.ebanckingbackend.dtos.operation.VirementUnitReqDTO;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.entities.VirementUnitaire;
import adria.sid.ebanckingbackend.exceptions.*;

public interface VirementService {
    void virementProgramme(VirementPermaReqDTO virementPermaReqDTO) throws DatesVirementPermanentAreNotValide, CompteNotExistException;
    void virementUnitaire(VirementUnitReqDTO virementUnitReqDTO) throws InsufficientBalanceException, MontantNotValide, CompteNotExistException, NotificationNotSended, OperationNotSaved;
}
