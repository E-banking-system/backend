package adria.sid.ebanckingbackend.repositories;

import adria.sid.ebanckingbackend.entities.Compte;
import adria.sid.ebanckingbackend.entities.Virement;
import adria.sid.ebanckingbackend.entities.VirementUnitaire;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VirementRepository extends JpaRepository<Virement, String> {
    @Query("SELECT v FROM Virement v WHERE v.user.id = :userId order by v.dateOperation desc ")
    Page<Virement> findByUserIdAndMontant(@Param("userId") String userId, Pageable pageable);

    @Query("SELECT COUNT(v) > 0 FROM Virement v WHERE v.beneficier.id = :beneficierId")
    boolean existsByBeneficierId(@Param("beneficierId") String beneficierId);
}