package adria.sid.ebanckingbackend;

import adria.sid.ebanckingbackend.entities.Beneficier;
import adria.sid.ebanckingbackend.repositories.BeneficierRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@SpringBootApplication
//@EnableScheduling
public class EbanckingBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(EbanckingBackendApplication.class, args);
    }

    /*@Scheduled(fixedDelay = 10000) // 10 seconds = 10,000 milliseconds
    public void printEleven() {
        System.out.println("Virement permanent a été effectuer avec succés le "+new Date()+".");
    }*/

    @Bean
    CommandLineRunner commandLineRunner(BeneficierRepository beneficierRepository) {
        return args -> {
            // Test BeneficierRepository methods here
            // For example:
            Beneficier beneficier = new Beneficier();
            beneficier.setId("BEN001");
            beneficier.setNom("John");
            beneficier.setPrenom("Doe");
            beneficier.setRIB("123456789");
            // Save the beneficier to the database
            beneficierRepository.save(beneficier);

            // Retrieve the beneficier from the database
            Beneficier retrievedBeneficier = beneficierRepository.findById("BEN001").orElse(null);
            if (retrievedBeneficier != null) {
                System.out.println("Retrieved beneficier: " + retrievedBeneficier);
            } else {
                System.out.println("Beneficier not found!");
            }

            // Test getBeneficiersByClientId method
            List<Beneficier> beneficiers = beneficierRepository.findByGerantId("CLIENT_ID_HERE");
            if (!beneficiers.isEmpty()) {
                System.out.println("Beneficiers for client with ID CLIENT_ID_HERE:");
                for (Beneficier b : beneficiers) {
                    System.out.println(b);
                }
            } else {
                System.out.println("No beneficiers found for client with ID CLIENT_ID_HERE");
            }
        };
    }
}
