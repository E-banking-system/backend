package adria.sid.ebanckingbackend.services.notification;

import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.entities.VirementProgramme;
import adria.sid.ebanckingbackend.entities.VirementUnitaire;

public interface NotificationCompteService {
    void saveVirementUnitaireEffectueNotification(VirementUnitaire virementUnitaire, UserEntity client, UserEntity beneficier);
    void clientCompteNotActive(VirementProgramme virementProgramme);
    void beneficierCompteNotActive(VirementProgramme virementProgramme);
    void activerCompte(String compteId,UserEntity user);
    void blockeCompte(String compteId,UserEntity user);
    void suspendCompte(String compteId,UserEntity user);
    void demandeSuspendCompte(String compteId,UserEntity user);
    void demandeBlockCompte(String compteId,UserEntity user);
    void demandeActivateCompte(String compteId,UserEntity user);
    void retraitCompte(String numCompte,UserEntity user,double montant,double newSolde);
    void depotCompte(String numCompte,UserEntity user,double montant,double newSolde);
    void soldeInsifisantCompte(String numCompte,UserEntity user);
    void virementToClientCompte(String numCompte,double montant);
}
