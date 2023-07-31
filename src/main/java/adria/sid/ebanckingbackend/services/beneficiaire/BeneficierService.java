package adria.sid.ebanckingbackend.services.beneficiaire;

import adria.sid.ebanckingbackend.entities.Beneficier;

import java.util.List;

public interface BeneficierService {
    void ajouterBeneficiair(Beneficier beneficier);
    void modifierBeneficier(Beneficier beneficier);
    void supprimerBeneficier(String beneficierId);
    Beneficier getBeneficierById(String beneficierId);
    List<Beneficier> getBeneficiersByClientId(String clientId);
}
