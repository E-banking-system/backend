package adria.sid.ebanckingbackend.services.virement;

import adria.sid.ebanckingbackend.dtos.virement.VirementPermanentReqDTO;
import adria.sid.ebanckingbackend.dtos.virement.VirementUnitReqDTO;
import adria.sid.ebanckingbackend.entities.*;
import adria.sid.ebanckingbackend.exceptions.BeneficierIsNotExistException;
import adria.sid.ebanckingbackend.exceptions.ClientIsNotExistException;
import adria.sid.ebanckingbackend.exceptions.CompteNotExistException;
import adria.sid.ebanckingbackend.repositories.BeneficierRepository;
import adria.sid.ebanckingbackend.repositories.CompteRepository;
import adria.sid.ebanckingbackend.repositories.UserRepository;
import adria.sid.ebanckingbackend.repositories.ViremenProgrammeRepository;
import adria.sid.ebanckingbackend.services.beneficiaire.BeneficierService;
import adria.sid.ebanckingbackend.services.compte.CompteService;
import adria.sid.ebanckingbackend.services.compte.CompteServiceImpl;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class VirementServiceImpl implements VirementService{
    final private CompteRepository compteRepository;
    final private BeneficierRepository beneficierRepository;
    final private UserRepository userRepository;
    final private CompteService compteService;
    final private ViremenProgrammeRepository viremenProgrammeRepository;
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

    @Override
    public void effectuerVirementPermanent(VirementPermanentReqDTO virementPermanentReqDTO) throws BeneficierIsNotExistException, ClientIsNotExistException, CompteNotExistException{
        /* 1) inserer dans la bases de données la liste des dates pour effectuer le virement programme
           2) envoyer une notification au client pour le informer que le virement programmé avec success
         */
        VirementProgramme virementProgramme=new VirementProgramme();
        virementProgramme.setId(UUID.randomUUID().toString());
        virementProgramme.setMontant(virementPermanentReqDTO.getMontant());
        virementProgramme.setFrequence(virementPermanentReqDTO.getFrequence());
        virementProgramme.setBeneficierId(virementPermanentReqDTO.getBeneficierId());
        virementProgramme.setClientId(virementPermanentReqDTO.getClientId());
        virementProgramme.setPrememierDateExecution(virementPermanentReqDTO.getPrememierDateExecution());
        virementProgramme.setDateFinExecution(virementPermanentReqDTO.getDateFinExecution());
        virementProgramme.setNumCompteBeneficier(virementPermanentReqDTO.getNumCompteBeneficier());
        virementProgramme.setNumCompteClient(virementPermanentReqDTO.getNumCompteClient());
        viremenProgrammeRepository.save(virementProgramme);
    }

    @Bean
    @Scheduled(fixedRate = 5000) // Run every 5000 milliseconds (5 seconds)
    public void effectuerVirementProgramme() throws CompteNotExistException, ClientIsNotExistException, BeneficierIsNotExistException {
        /* 1) verifier a chaque foix si la date courant egale a la date de virement programme (avec un decalage de 5 minute)
           2) si c'est le cas effectuer un virement unitaire
         */
        List<VirementProgramme> virementsProgramme=viremenProgrammeRepository.findPendingVirements(new Date());
        System.out.println("Les virement programme sont : "+virementsProgramme.size()+" virements");
        if(virementsProgramme.size()>0){
            for (VirementProgramme virementProgramme : virementsProgramme) {
                if (!virementProgramme.isEffectuer()) {
                    VirementUnitReqDTO virementUnitReqDTO = new VirementUnitReqDTO();
                    virementUnitReqDTO.setMontant(virementProgramme.getMontant());
                    virementUnitReqDTO.setBeneficierId(virementProgramme.getBeneficierId());
                    virementUnitReqDTO.setClientId(virementProgramme.getClientId());
                    virementUnitReqDTO.setNumCompteBeneficier(virementProgramme.getNumCompteBeneficier());
                    virementUnitReqDTO.setNumCompteClient(virementProgramme.getNumCompteClient());

                    effectuerVirementUnitaire(virementUnitReqDTO);
                }
                virementProgramme.setEffectuer(true);
                viremenProgrammeRepository.save(virementProgramme);
            }
        }
        System.out.println("Aucun virement programme a été effectuée");
    }
}
