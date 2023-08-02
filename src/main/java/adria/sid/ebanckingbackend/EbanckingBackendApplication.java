package adria.sid.ebanckingbackend;

import adria.sid.ebanckingbackend.ennumerations.EVType;
import adria.sid.ebanckingbackend.entities.VirementPermanant;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import java.util.Date;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class EbanckingBackendApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(EbanckingBackendApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        testGenererDatesExecution();
    }

    private void testGenererDatesExecution() {
        // Créez une instance de VirementPermanant avec les détails nécessaires
    }
}
