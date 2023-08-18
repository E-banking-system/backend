package adria.sid.ebanckingbackend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class EbanckingBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(EbanckingBackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {

        };
    }
}
