package adria.sid.ebanckingbackend.repositories;

import adria.sid.ebanckingbackend.entities.Compte;
import adria.sid.ebanckingbackend.entities.Operation;
import adria.sid.ebanckingbackend.entities.Virement;
import adria.sid.ebanckingbackend.entities.VirementUnitaire;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VirementRepository extends JpaRepository<Virement, String> {
    @Query("SELECT COUNT(v) > 0 FROM Virement v WHERE v.beneficier.beneficier_id = :beneficierId")
    boolean existsByBeneficierId(@Param("beneficierId") String beneficierId);
}