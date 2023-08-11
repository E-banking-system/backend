package adria.sid.ebanckingbackend.repositories;

import adria.sid.ebanckingbackend.entities.Depot;
import adria.sid.ebanckingbackend.entities.Retrait;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RetraitRepository extends JpaRepository<Retrait,String> {}
