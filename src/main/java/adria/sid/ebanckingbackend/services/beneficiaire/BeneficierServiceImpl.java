package adria.sid.ebanckingbackend.services.beneficiaire;

import adria.sid.ebanckingbackend.entities.Beneficier;
import adria.sid.ebanckingbackend.repositories.BeneficierRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class BeneficierServiceImpl implements BeneficierService {
    final private BeneficierRepository beneficiaireRepository;

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
    public List<Beneficier> getBeneficiersByClientId(String clientId) {
        return beneficiaireRepository.findByGerantId(clientId);
    }
}
