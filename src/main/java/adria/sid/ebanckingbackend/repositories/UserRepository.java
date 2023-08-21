package adria.sid.ebanckingbackend.repositories;

import adria.sid.ebanckingbackend.ennumerations.ERole;
import adria.sid.ebanckingbackend.entities.Compte;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.entities.Virement;
import adria.sid.ebanckingbackend.exceptions.UserAlreadyExists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,String> {
    Optional<UserEntity> findByEmail(String email) throws UserAlreadyExists;
    List<UserEntity> findByRole(ERole role);
    @Query("SELECT u FROM UserEntity u WHERE u.role = :role")
    Page<UserEntity> findAllUsersByRole(@Param("role") ERole role, Pageable pageable);
    @Query("SELECT u FROM UserEntity u INNER JOIN u.comptes c WHERE c.id = :compteId")
    UserEntity getUserByCompteId(@Param("compteId") String compteId);
    @Query("SELECT u FROM UserEntity u WHERE u.role = :role AND (LOWER(u.nom) LIKE CONCAT('%', :keyword, '%') OR LOWER(u.prenom) LIKE CONCAT('%', :keyword, '%') OR LOWER(u.address) LIKE CONCAT('%', :keyword, '%') OR LOWER(u.cin) LIKE CONCAT('%', :keyword, '%') OR LOWER(u.email) LIKE CONCAT('%', :keyword, '%') OR LOWER(u.tel) LIKE CONCAT('%', :keyword, '%'))")
    Page<UserEntity> searchClients(@Param("role") ERole role, Pageable pageable, @Param("keyword") String keyword);

}