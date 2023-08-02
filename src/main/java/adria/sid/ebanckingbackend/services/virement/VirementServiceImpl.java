package adria.sid.ebanckingbackend.services.virement;

import adria.sid.ebanckingbackend.dtos.virement.VirementUnitReqDTO;
import adria.sid.ebanckingbackend.entities.Beneficier;
import adria.sid.ebanckingbackend.entities.Compte;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.exceptions.BeneficierIsNotExistException;
import adria.sid.ebanckingbackend.exceptions.ClientIsNotExistException;
import adria.sid.ebanckingbackend.exceptions.CompteNotExistException;
import adria.sid.ebanckingbackend.repositories.BeneficierRepository;
import adria.sid.ebanckingbackend.repositories.CompteRepository;
import adria.sid.ebanckingbackend.repositories.UserRepository;
import adria.sid.ebanckingbackend.services.beneficiaire.BeneficierService;
import adria.sid.ebanckingbackend.services.compte.CompteService;
import adria.sid.ebanckingbackend.services.compte.CompteServiceImpl;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class VirementServiceImpl implements VirementService{
    final private CompteRepository compteRepository;
    final private BeneficierRepository beneficierRepository;
    final private UserRepository userRepository;
    final private CompteService compteService;
    @Transactional
    @Override
    public void effectuerVirementUnitaire(VirementUnitReqDTO viremenentReqDTO) throws BeneficierIsNotExistException, ClientIsNotExistException, CompteNotExistException {
        UserEntity client=userRepository.findById(viremenentReqDTO.getClientId()).orElse(null);
        if(client == null){
            throw new ClientIsNotExistException("Client is not valide");
        }

        Beneficier beneficier=beneficierRepository.findById(viremenentReqDTO.getBeneficierId()).orElse(null);
        if(beneficier == null){
            throw new BeneficierIsNotExistException("Beneficier is not valide");
        }

        Compte clientCompte = compteRepository.getCompteByNumCompte(viremenentReqDTO.getNumCompteClient());
        if(clientCompte == null){
            throw new CompteNotExistException("Ce numéro de compte n'existe pas : "+viremenentReqDTO.getNumCompteClient());
        }

        Compte beneficierCompte = compteRepository.getCompteByNumCompte(viremenentReqDTO.getNumCompteBeneficier());
        if(beneficierCompte == null){
            throw new CompteNotExistException("Ce numéro de compte n'existe pas : "+viremenentReqDTO.getNumCompteBeneficier());
        }

        compteService.changeSolde(clientCompte.getNumCompte(),-viremenentReqDTO.getMontant());
        compteService.changeSolde(beneficierCompte.getNumCompte(), viremenentReqDTO.getMontant());

        System.out.println("Virement effectuer avec succes");
    }
}
