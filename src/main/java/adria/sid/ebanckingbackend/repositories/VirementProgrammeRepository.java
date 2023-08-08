package adria.sid.ebanckingbackend.repositories;

import adria.sid.ebanckingbackend.entities.VirementProgramme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface VirementProgrammeRepository extends JpaRepository<VirementProgramme,String> {
    @Query("SELECT vp FROM VirementProgramme vp WHERE vp.dateExecution <= :currentDate AND vp.effectuer = false")
    List<VirementProgramme> findPendingVirements(@Param("currentDate") Date currentDate);
}
