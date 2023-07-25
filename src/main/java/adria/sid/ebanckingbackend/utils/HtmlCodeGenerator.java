package adria.sid.ebanckingbackend.utils;

import adria.sid.ebanckingbackend.entities.UserEntity;
import org.springframework.stereotype.Service;

@Service
public interface HtmlCodeGenerator {
    String generateVerifiedEmailHTML(String message);
    String generateActivatedEmailHTML(String url, UserEntity userEntity);
}
