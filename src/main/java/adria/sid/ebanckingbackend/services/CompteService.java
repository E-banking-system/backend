package adria.sid.ebanckingbackend.services;

import adria.sid.ebanckingbackend.dtos.ReqCreateAccountDTO;
import adria.sid.ebanckingbackend.ennumerations.EPType;
import adria.sid.ebanckingbackend.ennumerations.EtatCompte;
import adria.sid.ebanckingbackend.entities.Compte;
import adria.sid.ebanckingbackend.entities.EmailCorps;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.repositories.CompteRepository;
import adria.sid.ebanckingbackend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.task.TaskExecutor;
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

    private final EmailSender emailSender;



    @Transactional
    public void createAccountForExistingUserAndSendEmail(ReqCreateAccountDTO accountDTO) {
        Compte newCompte = new Compte();
        String sb=generateRandomDigit(24);
        String pin=generateRandomDigit(4);

        newCompte.setId(UUID.randomUUID().toString());
        newCompte.setNature(accountDTO.getNature());
        newCompte.setSolde(accountDTO.getSolde());
        newCompte.setRIB(sb);
        newCompte.setNumCompte(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE));
        newCompte.setDateCreation(accountDTO.getDateCreation());
        newCompte.setDatePeremption(accountDTO.getDatePeremption());
        newCompte.setDerniereDateSuspention(accountDTO.getDerniereDateSuspention());
        newCompte.setDerniereDateBloquage(accountDTO.getDerniereDateBloquage());
        newCompte.setEtatCompte(EtatCompte.ACTIVE);
        newCompte.setCodePIN(pin);

        compteRepository.save(newCompte);

        Optional<UserEntity> existingUserOptional = userRepository.findByEmail(accountDTO.getEmail());

        if (existingUserOptional.isPresent()) {
            UserEntity existingUser = existingUserOptional.get();

            existingUser.addCompte(newCompte);

            userRepository.save(existingUser);
        }
        UserEntity userEntity=new UserEntity();
        userEntity.setId(existingUserOptional.get().getId());
        userEntity.setEmail(existingUserOptional.get().getEmail());
        userEntity.setNom(existingUserOptional.get().getNom());
        userEntity.setPrenom(existingUserOptional.get().getPrenom());
        userEntity.setRaisonSociale(existingUserOptional.get().getRaisonSociale());

        emailSender.sendAccountInfosByEmail(userEntity,pin);
    }

    private String generateRandomDigit(int capacity){
        Random random = new Random();
        StringBuilder sb = new StringBuilder(capacity);
        for (int i = 0; i < 24; i++) {
            int digit = random.nextInt(10); // Generates a random digit (0 to 9)
            sb.append(digit);
        }
        return sb.toString();
    }

}
