package adria.sid.ebanckingbackend.utils.codeGenerators;

import org.springframework.stereotype.Service;

@Service
public interface CodeGenerator {
    String generatePinCode();
    String generateRIBCode();
    Long numeroCompte();
}
