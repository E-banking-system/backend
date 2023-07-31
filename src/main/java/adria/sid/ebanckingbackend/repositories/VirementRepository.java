package adria.sid.ebanckingbackend.repositories;

import adria.sid.ebanckingbackend.entities.VirementUnitaire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VirementRepository extends JpaRepository<VirementUnitaire,String> {
}
