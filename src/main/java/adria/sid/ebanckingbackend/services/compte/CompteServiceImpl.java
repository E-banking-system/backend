package adria.sid.ebanckingbackend.services.compte;

import adria.sid.ebanckingbackend.dtos.ReqCreateAccountDTO;
import adria.sid.ebanckingbackend.ennumerations.EtatCompte;
import adria.sid.ebanckingbackend.entities.Compte;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.repositories.CompteRepository;
import adria.sid.ebanckingbackend.repositories.UserRepository;
import adria.sid.ebanckingbackend.services.email.EmailSender;
import adria.sid.ebanckingbackend.utils.codeGenerators.CodeGenerator;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CompteServiceImpl implements CompteService {

    private final CompteRepository compteRepository;
    private final UserRepository userRepository;
    private final EmailSender emailSender;
    private final CodeGenerator codeGenerator;

    public CompteServiceImpl(CompteRepository compteRepository, UserRepository userRepository,
                             EmailSender emailSender, CodeGenerator codeGenerator) {
        this.compteRepository = compteRepository;
        this.userRepository = userRepository;
        this.emailSender = emailSender;
        this.codeGenerator = codeGenerator;
    }

    @Override
    @Transactional
    public void createAccountForExistingUserAndSendEmail(ReqCreateAccountDTO accountDTO) {
        // Input validation
        if (accountDTO == null || accountDTO.getEmail() == null) {
            throw new IllegalArgumentException("Invalid accountDTO");
        }

        Compte newCompte = new Compte();
        String rib = codeGenerator.generateRIBCode();
        String pin = codeGenerator.generatePinCode();

        newCompte.setId(UUID.randomUUID().toString());
        newCompte.setNature(accountDTO.getNature());
        newCompte.setSolde(accountDTO.getSolde());
        newCompte.setRIB(rib);
        newCompte.setNumCompte(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE));
        newCompte.setDateCreation(new Date());
        newCompte.setDatePeremption(null);
        newCompte.setDerniereDateSuspention(null);
        newCompte.setDerniereDateBloquage(null);
        newCompte.setEtatCompte(EtatCompte.ACTIVE);
        newCompte.setCodePIN(pin);

        compteRepository.save(newCompte);

        // Get the existing user if present
        Optional<UserEntity> existingUserOptional = userRepository.findByEmail(accountDTO.getEmail());

        if (existingUserOptional.isPresent()) {
            UserEntity existingUser = existingUserOptional.get();
            existingUser.addCompte(newCompte);
            userRepository.save(existingUser);

            // Send account info email to existing user
            emailSender.sendAccountInfosByEmail(existingUser, pin);
        } else {
            System.out.println("The is user is not found.");
        }
    }
}
