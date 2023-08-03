package adria.sid.ebanckingbackend.services.virement;

import adria.sid.ebanckingbackend.dtos.virement.VirementPermanentReqDTO;
import adria.sid.ebanckingbackend.dtos.virement.VirementUnitReqDTO;
import adria.sid.ebanckingbackend.ennumerations.EVType;
import adria.sid.ebanckingbackend.ennumerations.EtatCompte;
import adria.sid.ebanckingbackend.entities.*;
import adria.sid.ebanckingbackend.exceptions.*;
import adria.sid.ebanckingbackend.mappers.VirementMapper;
import adria.sid.ebanckingbackend.repositories.*;
import adria.sid.ebanckingbackend.services.compte.CompteService;
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
    final private VirementPermanantRepository virementPermanantRepository;
    final private VirementUnitaireRepository virementUnitaireRepository;
    final private VirementMapper virementMapper;

    @Transactional
    @Override
    public void effectuerVirementUnitaire(VirementUnitReqDTO viremenentReqDTO) throws  CompteNotExistException, MontantNotValide {
        if(viremenentReqDTO.getMontant()<100){
            throw new MontantNotValide("Ce montant n'est pas valide");
        }

        Compte clientCompte=compteRepository.getCompteByNumCompte(viremenentReqDTO.getNumCompteClient());
        if(clientCompte == null){
            throw new CompteNotExistException("Le compte client n'est pas valide");
        }

        Compte beneficierCompte=compteRepository.getCompteByNumCompte(viremenentReqDTO.getNumCompteBeneficier());
        if(beneficierCompte == null){
            throw new CompteNotExistException("Le compte beneficier n'est pas valide");
        }

        compteService.changeSolde(clientCompte.getNumCompte(),-viremenentReqDTO.getMontant());
        compteService.changeSolde(beneficierCompte.getNumCompte(), viremenentReqDTO.getMontant());

        VirementUnitaire virementUnitaire=virementMapper.fromVirementReqDTOToVirementUnitaire(viremenentReqDTO);
        virementUnitaireRepository.save(virementUnitaire);
        System.out.println("Virement effectuer avec succes");
    }

    /*@Override
    public void effectuerVirementPermanent(VirementPermanentReqDTO virementPermanentReqDTO) throws DatesVirementPermanentAreNotValide {
        if (virementPermanentReqDTO.getPrememierDateExecution().compareTo(virementPermanentReqDTO.getDateFinExecution()) > 0) {
            throw new DatesVirementPermanentAreNotValide("Les dates pour programmé ce virement ne sont pas valide");
        }

        VirementProgramme virementProgramme = new VirementProgramme();
        virementProgramme.setId(UUID.randomUUID().toString());
        virementProgramme.setMontant(virementPermanentReqDTO.getMontant());
        virementProgramme.setFrequence(EVType.valueOf(virementPermanentReqDTO.getFrequence()));
        virementProgramme.setBeneficierId(virementPermanentReqDTO.getBeneficierId());
        virementProgramme.setClientId(virementPermanentReqDTO.getClientId());
        virementProgramme.setPrememierDateExecution(virementPermanentReqDTO.getPrememierDateExecution());
        virementProgramme.setDateFinExecution(virementPermanentReqDTO.getDateFinExecution());
        virementProgramme.setNumCompteBeneficier(virementPermanentReqDTO.getNumCompteBeneficier());
        virementProgramme.setNumCompteClient(virementPermanentReqDTO.getNumCompteClient());

        // Générer la liste des dates d'exécution du virement
        List<Date> datesExecution = virementProgramme.genererDatesExecution();

        // Ajouter la liste des dates d'exécution du virement dans la base de données
        for (Date dateExecution : datesExecution) {
            System.out.println(dateExecution);
            VirementProgramme virementProgrammeExecution = new VirementProgramme();
            virementProgrammeExecution.setId(UUID.randomUUID().toString());
            virementProgrammeExecution.setPrememierDateExecution(dateExecution);
            virementProgrammeExecution.setDateFinExecution(virementProgramme.getDateFinExecution());
            virementProgrammeExecution.setEffectuer(false);
            virementProgrammeExecution.setMontant(virementProgramme.getMontant());
            virementProgrammeExecution.setFrequence(virementProgramme.getFrequence());
            virementProgrammeExecution.setNumCompteClient(virementProgramme.getNumCompteClient());
            virementProgrammeExecution.setNumCompteBeneficier(virementProgramme.getNumCompteBeneficier());
            virementProgrammeExecution.setClientId(virementProgramme.getClientId());
            virementProgrammeExecution.setBeneficierId(virementProgramme.getBeneficierId());

            // Enregistrer le virement programmé dans la base de données
            viremenProgrammeRepository.save(virementProgrammeExecution);
        }

        System.out.println("Virement programmé avec succès");
    }*/

    /*@Transactional
    @Override
    public void effectuerVirementPermanentNow(VirementUnitReqDTO viremenentReqDTO)  {
        UserEntity client=userRepository.findById(viremenentReqDTO.getClientId()).orElse(null);
        if(client == null){
            System.out.println("Client is not valide");
            return;
        }

        Beneficier beneficier=beneficierRepository.findById(viremenentReqDTO.getBeneficierId()).orElse(null);
        if(beneficier == null){
            System.out.println("Beneficier is not valide");
            return;
        }

        Compte clientCompte = compteRepository.getCompteByNumCompte(viremenentReqDTO.getNumCompteClient());
        if(clientCompte == null){
            System.out.println("Ce numéro de compte n'existe pas : "+viremenentReqDTO.getNumCompteClient());
            return;
        }

        Compte beneficierCompte = compteRepository.getCompteByNumCompte(viremenentReqDTO.getNumCompteBeneficier());
        if(beneficierCompte == null){
            System.out.println("Ce numéro de compte n'existe pas : "+viremenentReqDTO.getNumCompteBeneficier());
            return;
        }

        if(!(clientCompte.getEtatCompte() == EtatCompte.ACTIVE)){
            System.out.println("Ce compte n'est pas active : "+viremenentReqDTO.getNumCompteClient());
            return;
        }

        if(viremenentReqDTO.getMontant()>0){
            if((clientCompte.getSolde()-viremenentReqDTO.getMontant())<=0){
                System.out.println("Balance is not sufisante pour ce compte : "+viremenentReqDTO.getNumCompteClient());
                return;
            }
        } else{
            if((beneficierCompte.getSolde()-viremenentReqDTO.getMontant())<=0){
                System.out.println("Balance is not sufisante pour ce compte : "+viremenentReqDTO.getBeneficierId());
                return;
            }
        }

        compteService.changeSolde(clientCompte.getNumCompte(),-viremenentReqDTO.getMontant());
        compteService.changeSolde(beneficierCompte.getNumCompte(), viremenentReqDTO.getMontant());

        VirementPermanant virementPermanant=virementMapper.fromVirementReqDTOToVirementPermanent(viremenentReqDTO);
        virementPermanantRepository.save(virementPermanant);
        System.out.println("Virement effectuer avec succes");
    }*/

    /*@Bean
    @Scheduled(fixedRate = 5000) // Run every 5000 milliseconds (5 seconds)
    public void effectuerVirementProgramme() throws CompteNotExistException, ClientIsNotExistException, BeneficierIsNotExistException {*/
        /* 1) verifier a chaque foix si la date courant egale a la date de virement programme (avec un decalage de 5 minute)
           2) si c'est le cas effectuer un virement unitaire
         */
        /*List<VirementProgramme> virementsProgramme=viremenProgrammeRepository.findPendingVirements(new Date());
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

                    effectuerVirementPermanentNow(virementUnitReqDTO);
                }
                virementProgramme.setEffectuer(true);
                viremenProgrammeRepository.save(virementProgramme);
            }
        }
        System.out.println("Aucun virement programme a été effectuée");
    }*/
}
