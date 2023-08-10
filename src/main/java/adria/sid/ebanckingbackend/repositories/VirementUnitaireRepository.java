package adria.sid.ebanckingbackend.repositories;

import adria.sid.ebanckingbackend.entities.Operation;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.entities.VirementUnitaire;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VirementUnitaireRepository extends JpaRepository<VirementUnitaire,String> {
    @Query("SELECT v FROM VirementUnitaire v WHERE v.compte.id = :compteId")
    Page<VirementUnitaire> findByCompteId(Pageable pageable, @Param("compteId") String compteId);
}
