package adria.sid.ebanckingbackend.utils.codeGenerators;

import adria.sid.ebanckingbackend.entities.UserEntity;
import org.springframework.stereotype.Service;

@Service
public interface HtmlCodeGenerator {
    String generateVerifiedEmailHTML(String message);
    String generateActivatedEmailHTML(String url, String username);
    String generateActivatedAccountInfoEmail(String pin,String username);
    String generateResetPasswordEmailHTML(String username,String url);
    String generateOTPverificationHTML(String otpCode, String senderName);
}
