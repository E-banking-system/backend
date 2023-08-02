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
        VirementPermanant virementPermanant = new VirementPermanant();
        virementPermanant.setPrememierDateExecution(new Date()); // Remplacez par la date de début souhaitée
        virementPermanant.setDateFinExecution(new Date()); // Remplacez par la date de fin souhaitée
        virementPermanant.setFrequence(EVType.MENSUELLE); // Remplacez par la fréquence souhaitée

        // Générez la liste des dates d'exécution
        List<Date> datesExecution = virementPermanant.genererDatesExecution();

        // Affichez les dates générées
        System.out.println("Dates d'exécution générées :");
        for (Date date : datesExecution) {
            System.out.println(date);
        }
    }
}
