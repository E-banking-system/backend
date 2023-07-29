package adria.sid.ebanckingbackend.services.compte;

import adria.sid.ebanckingbackend.dtos.CompteDTO;
import adria.sid.ebanckingbackend.ennumerations.EtatCompte;
import adria.sid.ebanckingbackend.entities.Compte;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.mappers.CompteMapper;
import adria.sid.ebanckingbackend.repositories.CompteRepository;
import adria.sid.ebanckingbackend.repositories.UserRepository;
import adria.sid.ebanckingbackend.services.email.EmailSender;
import adria.sid.ebanckingbackend.utils.codeGenerators.CodeGenerator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@AllArgsConstructor
public class CompteServiceImpl implements CompteService {

    final private CompteRepository compteRepository;
    final private UserRepository userRepository;
    final private EmailSender emailSender;
    final private CodeGenerator codeGenerator;
    final private CompteMapper compteMapper;

    @Override
    @Transactional
    public void createAccountForExistingUserAndSendEmail(CompteDTO compteDTO) {
        // Input validation
        if (compteDTO == null || compteDTO.getEmail() == null) {
            throw new IllegalArgumentException("Invalid compte");
        }

        Compte newCompte = compteMapper.fromCompteDTOToCompte(compteDTO);

        // Get the existing user if present
        Optional<UserEntity> existingUserOptional = userRepository.findByEmail(compteDTO.getEmail());

        if (existingUserOptional.isPresent()) {
            compteRepository.save(newCompte);
            UserEntity existingUser = existingUserOptional.get();
            existingUser.addCompte(newCompte);
            userRepository.save(existingUser);

            // Send account info email to existing user
            emailSender.sendAccountInfosByEmail(existingUser, newCompte.getCodePIN());
        } else {
            System.out.println("The user is not found.");
        }
    }
}
