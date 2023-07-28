package adria.sid.ebanckingbackend.utils.codeGenerators;

import adria.sid.ebanckingbackend.entities.UserEntity;
import org.springframework.stereotype.Service;

@Service
public interface HtmlCodeGenerator {
    String generateVerifiedEmailHTML(String message);
    String generateActivatedEmailHTML(String url, UserEntity userEntity);
    String generateActivatedAccountInfoEmail(String pin,UserEntity userEntity);
    String generateResetPasswordEmailHTML(UserEntity userEntity,String url);
}
