package adria.sid.ebanckingbackend.repositories;

import adria.sid.ebanckingbackend.dtos.operation.OperationsCountByTimeDTO;
import adria.sid.ebanckingbackend.entities.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OperationRepository extends JpaRepository<Operation, String> {
    @Query(value = "SELECT * FROM operation o WHERE o.compte_id = :compteId OR ( (o.dtype = 'VirementUnitaire' OR o.dtype = 'VirementPermanant') AND o.beneficier_id IN (SELECT b.beneficier_id FROM beneficier b WHERE b.parent_user_id = :userId))", nativeQuery = true)
    Page<Operation> findByCompteId(Pageable pageable, @Param("compteId") String compteId, @Param("userId") String userId);

    @Query(value = "SELECT " +
            "   DATE_FORMAT(date_operation, '%Y-%m-%d') AS timeIntervalStart," +
            "   DATE_ADD(DATE_FORMAT(date_operation, '%Y-%m-%d'), INTERVAL 24 HOUR) AS timeIntervalEnd," +
            "   COUNT(*) AS operationsCount" +
            " FROM operation o" +
            " WHERE o.compte_id IN (SELECT c.id FROM compte c WHERE c.user_id = :userId)" +
            " GROUP BY DATE_FORMAT(date_operation, '%Y-%m-%d')",
            nativeQuery = true)
    List<Object[]> countOperationsByTimeRaw(@Param("userId") String userId);

}
