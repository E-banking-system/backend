package adria.sid.ebanckingbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EbanckingBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(EbanckingBackendApplication.class, args);
    }
}
