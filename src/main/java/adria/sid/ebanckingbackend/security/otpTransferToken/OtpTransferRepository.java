package adria.sid.ebanckingbackend.security.otpTransferToken;


import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.security.passwordToken.MotdepasseToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpTransferRepository extends JpaRepository<OtpTransferToken, Long> {
    OtpTransferToken findByToken(String theToken);
    OtpTransferToken findByUserIdAndToken(String userId,String token);
}