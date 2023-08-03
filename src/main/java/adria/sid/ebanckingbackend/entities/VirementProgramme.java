package adria.sid.ebanckingbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import adria.sid.ebanckingbackend.ennumerations.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static adria.sid.ebanckingbackend.ennumerations.EVType.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VirementProgramme {
    @Id
    private String id;

    private Double montant;
    private String numCompteClient;
    private String numCompteBeneficier;
    private String clientId;
    private String beneficierId;
    private Date prememierDateExecution;
    private Date dateFinExecution;

    @Enumerated(EnumType.STRING)
    private EVType frequence;

    private boolean effectuer=false;


    /*public List<Date> genererDatesExecution() {
        List<Date> datesExecution = new ArrayList<>();

        LocalDate dateDebut = prememierDateExecution.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate dateFin = dateFinExecution.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDate currentDate = dateDebut;
        while (!currentDate.isAfter(dateFin)) {
            datesExecution.add(Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

            switch (frequence) {
                case HEBDOMADAIRE -> currentDate = currentDate.plusWeeks(1);
                case BIMENSUELLE -> currentDate = currentDate.plusWeeks(2);
                case MENSUELLE -> currentDate = currentDate.plusMonths(1);
                case TRIMESTRIELLE -> currentDate = currentDate.plusMonths(3);
                case SEMESTRIELLE -> currentDate = currentDate.plusMonths(6);
                default -> {
                }
            }
        }

        return datesExecution;
    }*/
    public List<Date> genererDatesExecution() {
        List<Date> datesExecution = new ArrayList<>();

        LocalDateTime dateTimeDebut = prememierDateExecution.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
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
}
