package adria.sid.ebanckingbackend;

import adria.sid.ebanckingbackend.entities.Beneficier;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.repositories.BeneficierRepository;
import adria.sid.ebanckingbackend.services.beneficiaire.BeneficierService;
import adria.sid.ebanckingbackend.utils.codeGenerators.CodeGenerator;
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
import java.util.UUID;

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
    CommandLineRunner commandLineRunner(BeneficierService beneficierService,CodeGenerator codeGenerator) {
        return args -> {
            Beneficier beneficier=new Beneficier();
            UserEntity user=new UserEntity();
            user.setId("8cb789a2-0b82-422c-b17a-f086f4454a91");
            beneficier.setId(UUID.randomUUID().toString());
            beneficier.setPrenom("TAFFAH");
            beneficier.setNom("ACHRAF");
            beneficier.setCin("UU745888");
            beneficier.setAddress("RABAT");
            beneficier.setOperateur("ORANGE");
            beneficier.setRIB(codeGenerator.generateRIBCode());
            beneficier.setClient(user);
            beneficierService.ajouterBeneficiair(beneficier);
        };
    }
}
