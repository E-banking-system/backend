package adria.sid.ebanckingbackend.services.virement;

import adria.sid.ebanckingbackend.dtos.virement.VirementPermanentReqDTO;
import adria.sid.ebanckingbackend.dtos.virement.VirementUnitReqDTO;
import adria.sid.ebanckingbackend.exceptions.BeneficierIsNotExistException;
import adria.sid.ebanckingbackend.exceptions.ClientIsNotExistException;
import adria.sid.ebanckingbackend.exceptions.CompteNotExistException;


public interface VirementService {
    void effectuerVirementUnitaire(VirementUnitReqDTO viremenentReqDTO) throws BeneficierIsNotExistException, ClientIsNotExistException, CompteNotExistException;
    void effectuerVirementPermanent(VirementPermanentReqDTO virementPermanentReqDTO) throws BeneficierIsNotExistException, ClientIsNotExistException, CompteNotExistException;
}
