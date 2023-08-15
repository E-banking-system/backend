package adria.sid.ebanckingbackend.repositories;

import adria.sid.ebanckingbackend.entities.Compte;
import adria.sid.ebanckingbackend.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.*;
import java.util.List;

@Repository
public interface CompteRepository extends JpaRepository<Compte, String> {
    @Query("SELECT c FROM Compte c WHERE c.id = :compteId")
    Compte getCompteById(@Param("compteId") String compteId);

    @Query("SELECT c FROM Compte c WHERE c.numCompte = :numCompte")
    Compte getCompteByNumCompte(@Param("numCompte") String numCompte);

    @Query("SELECT c FROM Compte c WHERE LOWER(c.nature) LIKE %:keyword% OR c.rib LIKE %:keyword% OR LOWER(c.etatCompte) LIKE %:keyword% OR LOWER(c.id) LIKE %:keyword%")
    Page<Compte> searchComptes(Pageable pageable, @Param("keyword") String keyword);

    @Query("SELECT c FROM Compte c WHERE c.user.id = :userId AND (LOWER(c.nature) LIKE %:keyword% OR c.rib LIKE %:keyword% OR LOWER(c.etatCompte) LIKE %:keyword%)")
    Page<Compte> searchComptesByUserIdAndKeyword(@Param("userId") String userId, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT sum(c.solde) FROM Compte c WHERE c.user.id = :userId")
    Double getTotalSoldeByUserId(@Param("userId") String userId);
}
