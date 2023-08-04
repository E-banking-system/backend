package adria.sid.ebanckingbackend.repositories;

import adria.sid.ebanckingbackend.entities.Beneficier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BeneficierRepository extends JpaRepository<Beneficier, String> {
    List<Beneficier> findByClientId(String id);
    Beneficier getBeneficiersByNumCompte(String numCompte);
}
