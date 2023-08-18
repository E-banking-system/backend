package adria.sid.ebanckingbackend.utils.codeGenerators;

import adria.sid.ebanckingbackend.ennumerations.EVType;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CodeGeneratorImpl implements CodeGenerator{
    @Override
    public String generateOtpVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(1000000000); // Generate a random number between 0 and 99999999
        return String.format("%08d", code); // Format the number to have exactly 8 digits
    }

    @Override
    public String generatePinCode() {
        Random random = new Random();
        int pin = random.nextInt(10000); // Generate a random number between 0 and 9999
        return String.format("%04d", pin); // Format the number to have exactly 4 digits
    }

    @Override
    public String generateRIBCode(String banque, String ville, String numCompte, String cle) {
        // Validate input lengths
        if (banque.length() != 3 || !isNumeric(banque)) {
            throw new IllegalArgumentException("Banque must have 3 numeric digits.");
        }

        if (ville.length() != 3 || !isNumeric(ville)) {
            throw new IllegalArgumentException("Ville must have 3 numeric digits.");
        }

        if (numCompte.length() != 16 || !isNumeric(numCompte)) {
            throw new IllegalArgumentException("NumCompte must have 16 numeric digits.");
        }

        if (cle.length() != 2 || !isNumeric(cle)) {
            throw new IllegalArgumentException("Cle must have 2 numeric digits.");
        }

        return banque + ville + numCompte + cle;
    }

    // Helper method to check if a string is numeric
    private boolean isNumeric(String str) {
        return str.matches("\\d+");
    }

    @Override
    public String numeroCompte() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(16);
        for (int i = 0; i < 16; i++) {
            int digit = random.nextInt(10); // Generates a random digit (0 to 9)
            sb.append(digit);
        }
        return sb.toString();
    }

    //test version
    @Override
    public List<Date> genererDatesVirementPermanentVersionTest(Date premierDateExecution, Date dateFinExecution, EVType frequence) {
        List<Date> datesExecution = new ArrayList<>();

        LocalDateTime dateTimeDebut = premierDateExecution.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime dateTimeFin = dateFinExecution.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        LocalDateTime currentDateTime = dateTimeDebut;
        while (!currentDateTime.isAfter(dateTimeFin)) {
            datesExecution.add(Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant()));

            switch (frequence) {
                case HEBDOMADAIRE -> currentDateTime = currentDateTime.plusSeconds(10);
                case BIMENSUELLE -> currentDateTime = currentDateTime.plusSeconds(20);
                case MENSUELLE -> currentDateTime = currentDateTime.plusSeconds(40);
                case TRIMESTRIELLE -> currentDateTime = currentDateTime.plusSeconds(120);
                case SEMESTRIELLE -> currentDateTime = currentDateTime.plusSeconds(240);
                default -> {
                }
            }
        }

        return datesExecution;
    }

    //production version
    @Override
    public List<Date> genererDatesVirementPermanentVersionProduction(Date premierDateExecution, Date dateFinExecution, EVType frequence) {
        List<Date> datesExecution = new ArrayList<>();

        LocalDateTime dateTimeDebut = premierDateExecution.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime dateTimeFin = dateFinExecution.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        LocalDateTime currentDateTime = dateTimeDebut;
        while (!currentDateTime.isAfter(dateTimeFin)) {
            datesExecution.add(Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant()));

            switch (frequence) {
                case HEBDOMADAIRE -> currentDateTime = currentDateTime.plusWeeks(1);
                case BIMENSUELLE -> currentDateTime = currentDateTime.plusWeeks(2);
                case MENSUELLE -> currentDateTime = currentDateTime.plusMonths(1);
                case TRIMESTRIELLE -> currentDateTime = currentDateTime.plusMonths(3);
                case SEMESTRIELLE -> currentDateTime = currentDateTime.plusMonths(6);
                default -> {
                }
            }
        }

        return datesExecution;
    }
}
