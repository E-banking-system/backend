package adria.sid.ebanckingbackend.services.beneficiaire;

import adria.sid.ebanckingbackend.dtos.beneficier.BeneficierReqDTO;
import adria.sid.ebanckingbackend.dtos.beneficier.BeneficierResDTO;
import adria.sid.ebanckingbackend.ennumerations.EGender;
import adria.sid.ebanckingbackend.ennumerations.EPType;
import adria.sid.ebanckingbackend.entities.Beneficier;
import adria.sid.ebanckingbackend.entities.Compte;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.exceptions.BeneficierEmailIsNotExiste;
import adria.sid.ebanckingbackend.exceptions.CompteNotExistException;
import adria.sid.ebanckingbackend.exceptions.IdUserIsNotValideException;
import adria.sid.ebanckingbackend.mappers.BeneficierMapper;
import adria.sid.ebanckingbackend.repositories.BeneficierRepository;
import adria.sid.ebanckingbackend.repositories.CompteRepository;
import adria.sid.ebanckingbackend.repositories.UserRepository;
import adria.sid.ebanckingbackend.repositories.VirementRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class BeneficierServiceImpl implements BeneficierService {
    final private BeneficierRepository beneficiaireRepository;
    final private BeneficierMapper beneficierMapper;
    final private UserRepository userRepository;
    final private CompteRepository compteRepository;
    private final VirementRepository virementRepository;

    @Override
    public void ajouterBeneficiair(BeneficierReqDTO beneficierReqDTO) throws CompteNotExistException, BeneficierEmailIsNotExiste {
        Beneficier beneficier = beneficierMapper.fromBeneficierReqDTOToBeneficier(beneficierReqDTO);
        Compte compte = compteRepository.getCompteByNumCompte(beneficier.getNumCompte());

        if (compte == null) {
            throw new CompteNotExistException("Ce compte n'existe pas pour ce beneficier");
        }

        if (!Objects.equals(compte.getEtatCompte().toString(), "ACTIVE")) {
            throw new CompteNotExistException("Ce compte n'est pas actif");
        }

        UserEntity user=userRepository.findByEmail(beneficierReqDTO.getEmail()).orElseThrow(null);
        if(user == null){
            throw new BeneficierEmailIsNotExiste("This email is not related with any existed client");
        }
        beneficier.setParent_user(user);
        // Save the Beneficier entity
        beneficiaireRepository.save(beneficier);
    }


    @Override
    public void modifierBeneficier(BeneficierReqDTO beneficierReqDTO, String beneficierId) throws CompteNotExistException {
        Beneficier existingBeneficier = beneficiaireRepository.findById(beneficierId)
                .orElseThrow(() -> new EntityNotFoundException("Beneficier not found with ID: " + beneficierId));

        Compte compte = compteRepository.getCompteByNumCompte(beneficierReqDTO.getNumCompte());

        if (compte == null) {
            throw new CompteNotExistException("Ce compte n'existe pas pour ce beneficier");
        }

        Beneficier updatedBeneficier = beneficierMapper.fromBeneficierReqDTOToBeneficier(beneficierReqDTO);
        updatedBeneficier.setBeneficier_id(existingBeneficier.getBeneficier_id());
        updatedBeneficier.setParent_user(existingBeneficier.getUser());
        beneficiaireRepository.save(updatedBeneficier);
    }

    public void supprimerBeneficier(String beneficierId) throws Exception {
        if (isBeneficiaryReferencedInVirement(beneficierId)) {
            throw new Exception("Ce beneficier est une clé étrangère");
        }

        beneficiaireRepository.deleteById(beneficierId);
    }

    private boolean isBeneficiaryReferencedInVirement(String beneficierId) {
        return virementRepository.existsByBeneficierId(beneficierId);
    }

    @Override
    public Beneficier getBeneficierById(String beneficierId) {
        return beneficiaireRepository.findById(beneficierId).orElse(null);
    }

    @Override
    public Page<BeneficierResDTO> getBeneficiersByClientId(Pageable pageable, String clientId) {
        Optional<UserEntity> existingUser = userRepository.findById(clientId);
        if (existingUser.isEmpty()) {
            throw new IdUserIsNotValideException("Client is not exists: " + clientId);
        }

        List<Beneficier> beneficiers = beneficiaireRepository.findByUserId(clientId);
        List<BeneficierResDTO> beneficierResDTOList = beneficiers.stream()
                .map(beneficier -> {
                    BeneficierResDTO dto = beneficierMapper.fromBeneficierToBeneficierResDTO(beneficier);
                    dto.setEmail(beneficier.getUser().getEmail()); // Assuming there's a getUser() method in Beneficier to retrieve the related UserEntity
                    return dto;
                })
                .collect(Collectors.toList());

        log.info("Retrieved {} notifications for user ID: {}", beneficiers.size(), clientId);

        return new PageImpl<>(beneficierResDTOList, pageable, beneficiers.size());
    }
}
