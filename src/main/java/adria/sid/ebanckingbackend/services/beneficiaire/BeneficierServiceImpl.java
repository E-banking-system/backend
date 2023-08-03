package adria.sid.ebanckingbackend.services.beneficiaire;

import adria.sid.ebanckingbackend.dtos.beneficier.BeneficierReqDTO;
import adria.sid.ebanckingbackend.dtos.beneficier.BeneficierResDTO;
import adria.sid.ebanckingbackend.entities.Beneficier;
import adria.sid.ebanckingbackend.entities.Compte;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.exceptions.CompteNotExistException;
import adria.sid.ebanckingbackend.exceptions.IdUserIsNotValideException;
import adria.sid.ebanckingbackend.mappers.BeneficierMapper;
import adria.sid.ebanckingbackend.repositories.BeneficierRepository;
import adria.sid.ebanckingbackend.repositories.CompteRepository;
import adria.sid.ebanckingbackend.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class BeneficierServiceImpl implements BeneficierService {
    final private BeneficierRepository beneficiaireRepository;
    final private BeneficierMapper beneficierMapper;
    final private UserRepository userRepository;
    final private CompteRepository compteRepository;

    @Override
    public void ajouterBeneficiair(BeneficierReqDTO beneficierReqDTO) throws CompteNotExistException {
        Beneficier beneficier=beneficierMapper.fromBeneficierReqDTOToBeneficier(beneficierReqDTO);
        Compte compte=compteRepository.getCompteByNumCompte(beneficier.getNumCompte());
        if(compte == null){
            throw new CompteNotExistException("Ce compte nexiste pas pour ce beneficier");
        }

        beneficiaireRepository.save(beneficier);
    }

    @Override
    public void modifierBeneficier(BeneficierReqDTO beneficierReqDTO, String beneficierId) {
        Beneficier existingBeneficier = beneficiaireRepository.findById(beneficierId)
                .orElseThrow(() -> new EntityNotFoundException("Beneficier not found with ID: " + beneficierId));

        Beneficier updatedBeneficier = beneficierMapper.fromBeneficierReqDTOToBeneficier(beneficierReqDTO);
        updatedBeneficier.setId(existingBeneficier.getId());
        beneficiaireRepository.save(updatedBeneficier);
    }

    @Override
    public void supprimerBeneficier(String beneficierId) {
        beneficiaireRepository.deleteById(beneficierId);
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

        List<Beneficier> beneficiers = beneficiaireRepository.findByClientId(clientId);
        List<BeneficierResDTO> beneficierResDTOList = beneficiers.stream()
                .map(beneficierMapper::fromBeneficierToBeneficierResDTO)
                .collect(Collectors.toList());

        log.info("Retrieved {} notifications for user ID: {}", beneficiers.size(), clientId);

        return new PageImpl<>(beneficierResDTOList, pageable, beneficiers.size());
    }
}
