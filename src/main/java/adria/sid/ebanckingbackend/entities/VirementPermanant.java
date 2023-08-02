package adria.sid.ebanckingbackend.entities;

import adria.sid.ebanckingbackend.ennumerations.*;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class VirementPermanant extends Virement{
    private Boolean estPermanent=true;
    private Date prememierDateExecution;
    private Date dateFinExecution;

    @Enumerated(EnumType.STRING)
    private EVType frequence;

    public List<Date> genererDatesExecution() {
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
    }
}
