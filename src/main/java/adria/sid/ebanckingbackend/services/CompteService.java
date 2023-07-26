package adria.sid.ebanckingbackend.services;

import adria.sid.ebanckingbackend.dtos.ReqCreateAccountDTO;
import adria.sid.ebanckingbackend.ennumerations.EtatCompte;
import adria.sid.ebanckingbackend.entities.Compte;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.repositories.CompteRepository;
import adria.sid.ebanckingbackend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class CompteService {

    private final CompteRepository compteRepository;

    private final UserRepository userRepository;

    private final TaskExecutor taskExecutor;

    private final JavaMailSender javaMailSender;

    @Transactional
    public void createAccountForExistingUserAndSendEmail(ReqCreateAccountDTO accountDTO) {

        Random random = new Random();
        StringBuilder sb = new StringBuilder(24);
        for (int i = 0; i < 24; i++) {
            int digit = random.nextInt(10); // Generates a random digit (0 to 9)
            sb.append(digit);
        }

        Compte newCompte = new Compte();
        newCompte.setId(UUID.randomUUID().toString());
        newCompte.setNature(accountDTO.getNature());
        newCompte.setSolde(accountDTO.getSolde());
        newCompte.setRIB(sb.toString());
        newCompte.setNumCompte(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE));
        newCompte.setDateCreation(accountDTO.getDateCreation());
        newCompte.setDatePeremption(accountDTO.getDatePeremption());
        newCompte.setDerniereDateSuspention(accountDTO.getDerniereDateSuspention());
        newCompte.setDerniereDateBloquage(accountDTO.getDerniereDateBloquage());
        newCompte.setEtatCompte(EtatCompte.ACTIVE);

        compteRepository.save(newCompte);


        Optional<UserEntity> existingUserOptional = userRepository.findByEmail(accountDTO.getEmail());

        if (existingUserOptional.isPresent()) {
            UserEntity existingUser = existingUserOptional.get();

            existingUser.addCompte(newCompte);

            userRepository.save(existingUser);
        }

        taskExecutor.execute(() -> sendAccountCreationEmail(accountDTO.getEmail()));
    }

    private void sendAccountCreationEmail(String userEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setSubject("Compte créé");
        message.setText("Votre compte est bien activé, voici votre code PIN: " + );

        javaMailSender.send(message);

        System.out.println("Email sent successfully!");
    }

}
