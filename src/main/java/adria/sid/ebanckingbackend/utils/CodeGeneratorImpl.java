package adria.sid.ebanckingbackend.utils;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CodeGeneratorImpl implements CodeGenerator{
    @Override
    public String generatePinCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(24);
        for (int i = 0; i < 24; i++) {
            int digit = random.nextInt(10); // Generates a random digit (0 to 9)
            sb.append(digit);
        }
        return sb.toString();
    }

    @Override
    public String generateRIBCode() {
        Random random = new Random();
        int pin = random.nextInt(10000); // Generate a random number between 0 and 9999
        return String.format("%04d", pin); // Format the number to have exactly 4 digits
    }
}
