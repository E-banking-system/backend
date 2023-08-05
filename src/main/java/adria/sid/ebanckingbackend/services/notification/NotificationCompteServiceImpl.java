package adria.sid.ebanckingbackend.services.notification;

import adria.sid.ebanckingbackend.entities.*;
import adria.sid.ebanckingbackend.repositories.CompteRepository;
import adria.sid.ebanckingbackend.repositories.NotificationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationCompteServiceImpl implements NotificationCompteService {
    final private NotificationRepository notificationRepository;
    final private CompteRepository compteRepository;

    public void saveVirementUnitaireEffectueNotification(VirementUnitaire virementUnitaire, UserEntity client, UserEntity beneficier) {
        // Create a notification for the client
        Notification clientNotification = new Notification();
        clientNotification.setId(UUID.randomUUID().toString());
        clientNotification.setContenu("Un virement unitaire effectué avec succès par monsieur/madame : " +
                virementUnitaire.getUser().getNom() +" "+ virementUnitaire.getUser().getPrenom());
        clientNotification.setUser(client);
        clientNotification.setDateEnvoie(new Date());
        clientNotification.setTitre("Un virement unitaire de " + virementUnitaire.getMontant() + " DH effectué avec succès");

        notificationRepository.save(clientNotification);
        log.info("Virement effectue notification with ID: {}", clientNotification.getId());


        // Create a notification for the beneficier
        Notification beneficierNotification = new Notification();
        beneficierNotification.setId(UUID.randomUUID().toString());
        beneficierNotification.setContenu("Un virement unitaire effectué avec succès par monsieur/madame : " +
                virementUnitaire.getUser().getNom() +" "+ virementUnitaire.getUser().getPrenom());
        beneficierNotification.setUser(beneficier);
        beneficierNotification.setDateEnvoie(new Date());
        beneficierNotification.setTitre("Un virement unitaire de " + virementUnitaire.getMontant() + " DH effectué avec succès");

        notificationRepository.save(beneficierNotification);
        log.info("Virement unitaire effectue avec success");
    }

    @Override
    public void clientCompteNotActive(VirementProgramme virementProgramme) {
        Notification clientNotification = new Notification();
        clientNotification.setId(UUID.randomUUID().toString());

        Compte clientCompte=compteRepository.getCompteByNumCompte(virementProgramme.getNumCompteClient());
        UserEntity client=clientCompte.getUser();

        clientNotification.setContenu("Un virement permanant non effectué");
        clientNotification.setUser(client);
        clientNotification.setDateEnvoie(new Date());
        clientNotification.setTitre("Votre compte numéro "+virementProgramme.getNumCompteClient()+" n'est pas active");

        notificationRepository.save(clientNotification);
        log.info("le compte client n'est pas active");
    }

    @Override
    public void beneficierCompteNotActive(VirementProgramme virementProgramme) {
        Notification clientNotification = new Notification();
        clientNotification.setId(UUID.randomUUID().toString());

        Compte clientCompte=compteRepository.getCompteByNumCompte(virementProgramme.getNumCompteClient());
        UserEntity client=clientCompte.getUser();

        Compte beneficierCompte=compteRepository.getCompteByNumCompte(virementProgramme.getNumCompteBeneficier());
        UserEntity beneficier=beneficierCompte.getUser();

        clientNotification.setContenu("Un virement permanant non effectué");
        clientNotification.setUser(client);
        clientNotification.setDateEnvoie(new Date());
        clientNotification.setTitre("le compte beneficier de monsieur "+beneficier.getNom()+" "+beneficier.getPrenom()+"  n'est pas active");

        notificationRepository.save(clientNotification);
        log.info("le compte beneficier n'est pas active");
    }

    @Override
    public void activerCompte(String compteId,UserEntity user){
        Notification notification=new Notification();
        notification.setId(UUID.randomUUID().toString());
        notification.setContenu("Id compte : "+compteId);
        notification.setUser(user);
        notification.setDateEnvoie(new Date());
        notification.setTitre("Activation d'un compte effectuée avec success");
        notificationRepository.save(notification);
    }

    @Override
    public void blockeCompte(String compteId,UserEntity user){
        Notification notification=new Notification();
        notification.setId(UUID.randomUUID().toString());
        notification.setContenu("Id compte : "+compteId);
        notification.setUser(user);
        notification.setDateEnvoie(new Date());
        notification.setTitre("Bloackage d'un compte effectuée avec success");
        notificationRepository.save(notification);
    }

    @Override
    public void suspendCompte(String compteId,UserEntity user){
        Notification notification=new Notification();
        notification.setId(UUID.randomUUID().toString());
        notification.setContenu("Id compte : "+compteId);
        notification.setUser(user);
        notification.setDateEnvoie(new Date());
        notification.setTitre("Suspension d'un compte effectué avec success");
        notificationRepository.save(notification);
    }

    @Override
    public void demandeSuspendCompte(String compteId,UserEntity user){
        Notification notification=new Notification();
        notification.setId(UUID.randomUUID().toString());
        notification.setContenu("Id compte : "+compteId);
        notification.setUser(user);
        notification.setDateEnvoie(new Date());
        notification.setTitre("Demande de suspension d'un compte");
        notificationRepository.save(notification);
    }

    @Override
    public void demandeBlockCompte(String compteId,UserEntity user){
        Notification notification=new Notification();
        notification.setId(UUID.randomUUID().toString());
        notification.setContenu("Id compte : "+compteId);
        notification.setUser(user);
        notification.setDateEnvoie(new Date());
        notification.setTitre("Demande de blockage d'un compte");
        notificationRepository.save(notification);
    }

    @Override
    public void demandeActivateCompte(String compteId,UserEntity user){
        Notification notification=new Notification();
        notification.setId(UUID.randomUUID().toString());
        notification.setContenu("Id compte : "+compteId);
        notification.setUser(user);
        notification.setDateEnvoie(new Date());
        notification.setTitre("Demande d'activation d'un compte");
        notificationRepository.save(notification);
    }

    @Override
    public void retraitCompte(String numCompte,UserEntity user,double montant,double newSolde){
        Notification notification = new Notification();
        notification.setId(UUID.randomUUID().toString());
        notification.setContenu("Un retrait de "+montant+" a été effectueé avec success");
        notification.setUser(user);
        notification.setDateEnvoie(new Date());
        notification.setTitre("Numéro du compte : " + numCompte);
        notificationRepository.save(notification);
    }

    @Override
    public void depotCompte(String numCompte,UserEntity user,double montant,double newSolde){
        Notification notification = new Notification();
        notification.setId(UUID.randomUUID().toString());
        notification.setContenu("Un dépot de "+montant+" a été effectueé avec success");
        notification.setUser(user);
        notification.setDateEnvoie(new Date());
        notification.setTitre("Numéro du compte : " + numCompte);
        notificationRepository.save(notification);
    }
    @Override
    public void soldeInsifisantCompte(String numCompte,UserEntity user){
        Notification notification = new Notification();
        notification.setId(UUID.randomUUID().toString());
        notification.setContenu("Un virement n'a pas été effectué car le montant est insuffisant.");
        notification.setUser(user);
        notification.setDateEnvoie(new Date());
        notification.setTitre("Numéro du compte : " + numCompte);
        notificationRepository.save(notification);
    }

    @Override
    public void virementPermanentToClientCompte(String numCompte,double montant){
        Notification clientNotification = new Notification();
        clientNotification.setId(UUID.randomUUID().toString());
        Compte compte = compteRepository.getCompteByNumCompte(numCompte);
        clientNotification.setContenu("VIREMENT WEB PROGRAMMÉ REÇU DE "+compte.getUser().getNom() +" "+ compte.getUser().getPrenom());
        clientNotification.setUser(compte.getUser());
        clientNotification.setDateEnvoie(new Date());
        clientNotification.setTitre("+"+montant+"DH");
        notificationRepository.save(clientNotification);
    }

    @Override
    public void virementPermanentToBeneficierCompte(String numCompte,double montant){
        Notification clientNotification = new Notification();
        clientNotification.setId(UUID.randomUUID().toString());
        Compte compte = compteRepository.getCompteByNumCompte(numCompte);
        clientNotification.setContenu("VIREMENT WEB PROGRAMMÉ ENVOYÉ A "+compte.getUser().getNom() +" "+ compte.getUser().getPrenom());
        clientNotification.setUser(compte.getUser());
        clientNotification.setDateEnvoie(new Date());
        clientNotification.setTitre(montant+"DH");
        notificationRepository.save(clientNotification);
    }

    @Override
    public void virementUnitaireToClientCompte(String numCompte,double montant){
        Notification clientNotification = new Notification();
        clientNotification.setId(UUID.randomUUID().toString());
        Compte compte = compteRepository.getCompteByNumCompte(numCompte);
        clientNotification.setContenu("VIREMENT UNITAIRE REÇU DE "+compte.getUser().getNom() +" "+ compte.getUser().getPrenom());
        clientNotification.setUser(compte.getUser());
        clientNotification.setDateEnvoie(new Date());
        clientNotification.setTitre("+"+montant+"DH");
        notificationRepository.save(clientNotification);
    }

    @Override
    public void virementUnitaireToBeneficierCompte(String numCompte,double montant){
        Notification clientNotification = new Notification();
        clientNotification.setId(UUID.randomUUID().toString());
        Compte compte = compteRepository.getCompteByNumCompte(numCompte);
        clientNotification.setContenu("VIREMENT UNITAIRE ENVOYÉ A "+compte.getUser().getNom() +" "+ compte.getUser().getPrenom());
        clientNotification.setUser(compte.getUser());
        clientNotification.setDateEnvoie(new Date());
        clientNotification.setTitre(montant+"DH");
        notificationRepository.save(clientNotification);
    }
}
