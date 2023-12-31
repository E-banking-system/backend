package adria.sid.ebanckingbackend.services.beneficiaire;

import adria.sid.ebanckingbackend.dtos.beneficier.BeneficierReqDTO;
import adria.sid.ebanckingbackend.dtos.beneficier.BeneficierResDTO;
import adria.sid.ebanckingbackend.entities.Beneficier;
import adria.sid.ebanckingbackend.exceptions.BeneficierEmailIsNotExiste;
import adria.sid.ebanckingbackend.exceptions.CompteNotExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BeneficierService {
    void ajouterBeneficiair(BeneficierReqDTO beneficierReqDTO) throws CompteNotExistException, BeneficierEmailIsNotExiste;
    void modifierBeneficier(BeneficierReqDTO beneficierReqDTO, String beneficierId) throws CompteNotExistException;
    void supprimerBeneficier(String beneficierId) throws Exception;
    Beneficier getBeneficierById(String beneficierId);
    Page<BeneficierResDTO> getBeneficiersByClientId(Pageable pageable, String clientId);
}
