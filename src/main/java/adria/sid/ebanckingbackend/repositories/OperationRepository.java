package adria.sid.ebanckingbackend.repositories;

import adria.sid.ebanckingbackend.entities.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OperationRepository extends JpaRepository<Operation,String> {
    @Query("SELECT o FROM Operation o WHERE o.compte.id = :compteId")
    Page<Operation> getCompteOperations(
            Pageable pageable,
            @Param("compteId") String compteId
            );
}
