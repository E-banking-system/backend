package adria.sid.ebanckingbackend.services.notification;

import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.entities.VirementProgramme;
import adria.sid.ebanckingbackend.entities.VirementUnitaire;

public interface NotificationServiceVirement{
    void saveVirementUnitaireEffectueNotification(VirementUnitaire virementUnitaire, UserEntity client, UserEntity beneficier);
    void clientCompteNotExists(VirementProgramme virementProgramme);
    void beneficierCompteNotExists(VirementProgramme virementProgramme);
    void clientCompteNotActive(VirementProgramme virementProgramme);
    void beneficierCompteNotActive(VirementProgramme virementProgramme);
}
