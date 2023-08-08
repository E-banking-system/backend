package adria.sid.ebanckingbackend.services.notification;

import adria.sid.ebanckingbackend.entities.*;
import adria.sid.ebanckingbackend.exceptions.NotificationNotSended;
import adria.sid.ebanckingbackend.repositories.NotificationRepository;
import adria.sid.ebanckingbackend.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class OperationNotificationServiceImpl implements OperationNotificationService {
    final private NotificationRepository notificationRepository;
    final private UserRepository userRepository;

    @Override
    public void sendDepotNotificationToClient(Compte compte, double montant) throws NotificationNotSended {
        try {
            UserEntity client=userRepository.getUserByCompteId(compte.getId());
            Notification notification = new Notification();
            notification.setId(UUID.randomUUID().toString());
            notification.setContenu("Un depot de "+montant+" a été effectueé avec success");
            notification.setUser(client);
            notification.setDateEnvoie(new Date());
            notification.setTitre("Numéro du compte : " + compte.getNumCompte());
            notificationRepository.save(notification);
        } catch (Exception e){
            throw new NotificationNotSended("This notification is not sended");
        }
    }

    @Override
    public void sendRetraitNotificationToClient(Compte compte, double montant) throws NotificationNotSended {
        try {
            UserEntity client=userRepository.getUserByCompteId(compte.getId());
            Notification notification = new Notification();
            notification.setId(UUID.randomUUID().toString());
            notification.setContenu("Un retrait de "+montant+" a été effectueé avec success");
            notification.setUser(client);
            notification.setDateEnvoie(new Date());
            notification.setTitre("Numéro du compte : " + compte.getNumCompte());
            notificationRepository.save(notification);
        } catch (Exception e){
            throw new NotificationNotSended("This notification is not sended");
        }
    }

    @Override
    public void sendVirementUnitNotificationToClient(Compte compte, double montant) throws NotificationNotSended {
        try {
            Notification clientNotification = new Notification();
            clientNotification.setId(UUID.randomUUID().toString());
            clientNotification.setContenu("VIREMENT UNITAIRE REÇU DE "+compte.getUser().getNom() +" "+ compte.getUser().getPrenom());
            clientNotification.setUser(compte.getUser());
            clientNotification.setDateEnvoie(new Date());
            clientNotification.setTitre("+"+montant+"DH");
            notificationRepository.save(clientNotification);
        } catch (Exception e){
            throw new NotificationNotSended("This notification is not sended");
        }
    }

    @Override
    public void sendVirementUnitNotificationToBeneficier(Compte compte, double montant) throws NotificationNotSended {
        try {
            Notification clientNotification = new Notification();
            clientNotification.setId(UUID.randomUUID().toString());
            clientNotification.setContenu("VIREMENT UNITAIRE ENVOYÉ A "+compte.getUser().getNom() +" "+ compte.getUser().getPrenom());
            clientNotification.setUser(compte.getUser());
            clientNotification.setDateEnvoie(new Date());
            clientNotification.setTitre(montant+"DH");
            notificationRepository.save(clientNotification);
        } catch (Exception e){
            throw new NotificationNotSended("This notification is not sended");
        }
    }

    @Override
    public void sendClientCompteNotActiveNotificationToClient(Compte clientCompte) throws NotificationNotSended {
        try {
            Notification clientNotification = new Notification();
            clientNotification.setId(UUID.randomUUID().toString());

            UserEntity client=clientCompte.getUser();

            clientNotification.setContenu("Un virement permanant non effectué");
            clientNotification.setUser(client);
            clientNotification.setDateEnvoie(new Date());
            clientNotification.setTitre("Votre compte numéro "+clientCompte.getNumCompte()+" n'est pas active");

            notificationRepository.save(clientNotification);
        } catch (Exception e){
            throw new NotificationNotSended("This notification is not sended");
        }
    }

    @Override
    public void sendBeneficierCompteNotActiveNotificationToClient(Compte clientCompte, Compte beneficierCompte) throws NotificationNotSended {
        try {
            Notification clientNotification = new Notification();
            clientNotification.setId(UUID.randomUUID().toString());

            UserEntity client=clientCompte.getUser();
            UserEntity beneficier=beneficierCompte.getUser();

            clientNotification.setContenu("Un virement permanant non effectué");
            clientNotification.setUser(client);
            clientNotification.setDateEnvoie(new Date());
            clientNotification.setTitre("le compte beneficier de monsieur " + beneficier.getNom()+" "+beneficier.getPrenom()+" n'est pas active");

            notificationRepository.save(clientNotification);
        } catch (Exception e){
            throw new NotificationNotSended("This notification is not sended");
        }
    }

    @Override
    public void sendVirementPermanentNotificationToClientCompte(Compte clientCompte, double montant) throws NotificationNotSended {
        try {
            Notification clientNotification = new Notification();
            clientNotification.setId(UUID.randomUUID().toString());
            clientNotification.setContenu("VIREMENT WEB PROGRAMMÉ REÇU DE "+clientCompte.getUser().getNom() +" "+ clientNotification.getUser().getPrenom());
            clientNotification.setUser(clientCompte.getUser());
            clientNotification.setDateEnvoie(new Date());
            clientNotification.setTitre("+"+montant+"DH");
            notificationRepository.save(clientNotification);
        } catch (Exception e){
            throw new NotificationNotSended("This notification is not sended");
        }
    }

    @Override
    public void sendVirementPermanentNotificationToBeneficierCompte(Compte beneficierCompte, double montant) throws NotificationNotSended {
        try {
            Notification clientNotification = new Notification();
            clientNotification.setId(UUID.randomUUID().toString());
            clientNotification.setContenu("VIREMENT WEB PROGRAMMÉ ENVOYÉ A "+beneficierCompte.getUser().getNom() +" "+ beneficierCompte.getUser().getPrenom());
            clientNotification.setUser(beneficierCompte.getUser());
            clientNotification.setDateEnvoie(new Date());
            clientNotification.setTitre(montant+"DH");
            notificationRepository.save(clientNotification);
        } catch (Exception e){
            throw new NotificationNotSended("This notification is not sended");
        }
    }

    @Override
    public void sendActiverCompteNotificationToClient(String compteId, UserEntity user) throws NotificationNotSended {
        try {
            Notification notification=new Notification();
            notification.setId(UUID.randomUUID().toString());
            notification.setContenu("Id compte : "+compteId);
            notification.setUser(user);
            notification.setDateEnvoie(new Date());
            notification.setTitre("Activation d'un compte effectuée avec success");
            notificationRepository.save(notification);
        } catch (Exception e){
            throw new NotificationNotSended("This notification is not sended");
        }
    }

    @Override
    public void sendBlockeCompteNotificationToClient(String compteId,UserEntity user) throws NotificationNotSended {
        try {
            Notification notification=new Notification();
            notification.setId(UUID.randomUUID().toString());
            notification.setContenu("Id compte : "+compteId);
            notification.setUser(user);
            notification.setDateEnvoie(new Date());
            notification.setTitre("Bloackage d'un compte effectuée avec success");
            notificationRepository.save(notification);
        } catch (Exception e){
            throw new NotificationNotSended("This notification is not sended");
        }
    }

    @Override
    public void sendSuspendCompteNotificationToClient(String compteId,UserEntity user) throws NotificationNotSended {
        try {
            Notification notification=new Notification();
            notification.setId(UUID.randomUUID().toString());
            notification.setContenu("Id compte : "+compteId);
            notification.setUser(user);
            notification.setDateEnvoie(new Date());
            notification.setTitre("Suspension d'un compte effectué avec success");
            notificationRepository.save(notification);
        } catch (Exception e){
            throw new NotificationNotSended("This notification is not sended");
        }
    }

    @Override
    public void sendDemandeSuspendCompteNotificationToBanquier(String compteId,UserEntity user) throws NotificationNotSended {
        try {
            Notification notification=new Notification();
            notification.setId(UUID.randomUUID().toString());
            notification.setContenu("Id compte : "+compteId);
            notification.setUser(user);
            notification.setDateEnvoie(new Date());
            notification.setTitre("Demande de suspension d'un compte");
            notificationRepository.save(notification);
        } catch (Exception e){
            throw new NotificationNotSended("This notification is not sended");
        }
    }

    @Override
    public void sendDemandeBlockCompteNotificationToBanquier(String compteId,UserEntity user) throws NotificationNotSended {
        try {
            Notification notification=new Notification();
            notification.setId(UUID.randomUUID().toString());
            notification.setContenu("Id compte : "+compteId);
            notification.setUser(user);
            notification.setDateEnvoie(new Date());
            notification.setTitre("Demande de blockage d'un compte");
            notificationRepository.save(notification);
        } catch (Exception e){
            throw new NotificationNotSended("This notification is not sended");
        }
    }

    @Override
    public void sendDemandeActivateCompteNotificationToBanquier(String compteId,UserEntity user) throws NotificationNotSended {
        try {
            Notification notification=new Notification();
            notification.setId(UUID.randomUUID().toString());
            notification.setContenu("Id compte : "+compteId);
            notification.setUser(user);
            notification.setDateEnvoie(new Date());
            notification.setTitre("Demande d'activation d'un compte");
            notificationRepository.save(notification);
        } catch (Exception e){
            throw new NotificationNotSended("This notification is not sended");
        }
    }

    @Override
    public void sendSoldeInsifisantCompteNotificationToClient(String numCompte,UserEntity user) throws NotificationNotSended {
        try {
            Notification notification = new Notification();
            notification.setId(UUID.randomUUID().toString());
            notification.setContenu("Un virement n'a pas été effectué car le montant est insuffisant.");
            notification.setUser(user);
            notification.setDateEnvoie(new Date());
            notification.setTitre("Numéro du compte : " + numCompte);
            notificationRepository.save(notification);
        } catch (Exception e){
            throw new NotificationNotSended("This notification is not sended");
        }
    }
}
