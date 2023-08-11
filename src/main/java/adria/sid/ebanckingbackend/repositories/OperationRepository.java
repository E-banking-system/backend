package adria.sid.ebanckingbackend.repositories;

import adria.sid.ebanckingbackend.entities.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OperationRepository extends JpaRepository<Operation, String> {
    @Query(value = "SELECT * FROM operation o WHERE o.compte_id = :compteId OR (o.dtype = 'VirementUnitaire' AND o.beneficier_id IN (SELECT b.beneficier_id FROM beneficier b WHERE b.parent_user_id = :userId))", nativeQuery = true)
    Page<Operation> findByCompteId(Pageable pageable, @Param("compteId") String compteId, @Param("userId") String userId);
}
