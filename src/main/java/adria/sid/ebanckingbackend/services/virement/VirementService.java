package adria.sid.ebanckingbackend.services.virement;

import adria.sid.ebanckingbackend.dtos.virement.VirementPermanentReqDTO;
import adria.sid.ebanckingbackend.dtos.virement.VirementUnitReqDTO;
import adria.sid.ebanckingbackend.entities.VirementProgramme;
import adria.sid.ebanckingbackend.exceptions.*;
import jakarta.transaction.Transactional;


public interface VirementService {
    void effectuerVirementUnitaire(VirementUnitReqDTO viremenentReqDTO) throws BeneficierIsNotExistException, ClientIsNotExistException, CompteNotExistException, MontantNotValide;

    void effectuerVirementPermanentAsync(VirementProgramme virementProgramme);

    void effectuerVirementPermanent(VirementPermanentReqDTO virementPermanentReqDTO) throws DatesVirementPermanentAreNotValide;
}
