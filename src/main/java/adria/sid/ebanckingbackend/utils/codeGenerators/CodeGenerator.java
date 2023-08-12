package adria.sid.ebanckingbackend.utils.codeGenerators;

import adria.sid.ebanckingbackend.ennumerations.EVType;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface CodeGenerator {
    String generatePinCode();
    String generateRIBCode();
    Long numeroCompte();
    List<Date> genererDatesVirementPermanentVersionTest(Date premierDateExecution, Date dateFinExecution, EVType frequence);
    List<Date> genererDatesVirementPermanentVersionProduction(Date premierDateExecution, Date dateFinExecution, EVType frequence);
}
