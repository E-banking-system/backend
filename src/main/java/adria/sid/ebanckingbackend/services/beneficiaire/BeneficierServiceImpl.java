package adria.sid.ebanckingbackend.services.beneficiaire;

import adria.sid.ebanckingbackend.dtos.beneficier.BeneficierResDTO;
import adria.sid.ebanckingbackend.dtos.notification.NotificationResDTO;
import adria.sid.ebanckingbackend.entities.Beneficier;
import adria.sid.ebanckingbackend.entities.Notification;
import adria.sid.ebanckingbackend.mappers.BeneficierMapper;
import adria.sid.ebanckingbackend.repositories.BeneficierRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class BeneficierServiceImpl implements BeneficierService {
    final private BeneficierRepository beneficiaireRepository;
    final private BeneficierMapper beneficierMapper;

    @Override
    public void ajouterBeneficiair(Beneficier beneficier) {
        beneficiaireRepository.save(beneficier);
    }

    @Override
    public void modifierBeneficier(Beneficier beneficier) {
        log.info("Updated beneficier with ID: {}", beneficier.getId());
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
        List<Beneficier> beneficiers = beneficiaireRepository.findByClientId(clientId);
        List<BeneficierResDTO> beneficierResDTOList = beneficiers.stream()
                .map(beneficierMapper::fromBeneficierToBeneficierResDTO)
                .collect(Collectors.toList());

        log.info("Retrieved {} notifications for user ID: {}", beneficiers.size(), clientId);

        return new PageImpl<>(beneficierResDTOList, pageable, beneficiers.size());
    }
}
