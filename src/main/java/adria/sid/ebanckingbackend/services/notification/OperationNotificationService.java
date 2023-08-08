package adria.sid.ebanckingbackend.services.notification;

import adria.sid.ebanckingbackend.entities.Compte;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.entities.VirementProgramme;
import adria.sid.ebanckingbackend.entities.VirementUnitaire;
import adria.sid.ebanckingbackend.exceptions.NotificationNotSended;

public interface OperationNotificationService {
    void sendDepotNotificationToClient(Compte compte, double montant) throws NotificationNotSended;
    void sendRetraitNotificationToClient(Compte compte, double montant) throws NotificationNotSended;
    void sendVirementUnitNotificationToClient(Compte compte, double montant) throws NotificationNotSended;
    void sendVirementUnitNotificationToBeneficier(Compte compte, double montant) throws NotificationNotSended;
    void sendClientCompteNotActiveNotificationToClient(Compte clientCompte) throws NotificationNotSended;
    void sendBeneficierCompteNotActiveNotificationToClient(Compte clientCompte, Compte beneficierCompte) throws NotificationNotSended;
    void sendVirementPermanentNotificationToClientCompte(Compte clientCompte, double montant) throws NotificationNotSended;
    void sendVirementPermanentNotificationToBeneficierCompte(Compte beneficierCompte, double montant) throws NotificationNotSended;
    void sendActiverCompteNotificationToClient(String compteId, UserEntity user) throws NotificationNotSended;
    void sendBlockeCompteNotificationToClient(String compteId, UserEntity user) throws NotificationNotSended;
    void sendSuspendCompteNotificationToClient(String compteId, UserEntity user) throws NotificationNotSended;
    void sendDemandeSuspendCompteNotificationToBanquier(String compteId, UserEntity user) throws NotificationNotSended;
    void sendDemandeBlockCompteNotificationToBanquier(String compteId, UserEntity user) throws NotificationNotSended;
    void sendDemandeActivateCompteNotificationToBanquier(String compteId, UserEntity user) throws NotificationNotSended;
    void sendSoldeInsifisantCompteNotificationToClient(String numCompte, UserEntity user) throws NotificationNotSended;
}
