package adria.sid.ebanckingbackend.security.passwordToken;


import org.springframework.data.jpa.repository.JpaRepository;

public interface MotdePasseTokenRepository extends JpaRepository<MotdepasseToken, Long> {
    MotdepasseToken findByToken(String theToken);
}