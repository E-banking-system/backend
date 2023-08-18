package adria.sid.ebanckingbackend.utils.codeGenerators;

import adria.sid.ebanckingbackend.ennumerations.EVType;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface CodeGenerator {
    String generateOtpVerificationCode();
    String generatePinCode();
    String generateRIBCode(String banque, String ville, String numCompte, String cle);
    String numeroCompte();
    List<Date> genererDatesVirementPermanentVersionTest(Date premierDateExecution, Date dateFinExecution, EVType frequence);
    List<Date> genererDatesVirementPermanentVersionProduction(Date premierDateExecution, Date dateFinExecution, EVType frequence);
}
