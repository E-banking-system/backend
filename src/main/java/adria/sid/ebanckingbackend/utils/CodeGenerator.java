package adria.sid.ebanckingbackend.utils;

import org.springframework.stereotype.Service;

@Service
public interface CodeGenerator {
    String generatePinCode();
    String generateRIBCode();
}
