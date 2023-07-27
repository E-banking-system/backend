package adria.sid.ebanckingbackend.services;

import adria.sid.ebanckingbackend.dtos.ReqCreateAccountDTO;
import adria.sid.ebanckingbackend.ennumerations.EtatCompte;
import adria.sid.ebanckingbackend.entities.Compte;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.repositories.CompteRepository;
import adria.sid.ebanckingbackend.repositories.UserRepository;
import adria.sid.ebanckingbackend.utils.CodeGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class CompteServiceImpl implements CompteService{

    private final CompteRepository compteRepository;

    private final UserRepository userRepository;

    private final EmailSender emailSender;

    private final CodeGenerator codeGenerator;

    @Override
    @Transactional
    public void createAccountForExistingUserAndSendEmail(ReqCreateAccountDTO accountDTO) {
        Compte newCompte = new Compte();
        String rib=codeGenerator.generateRIBCode();
        String pin= codeGenerator.generatePinCode();

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
}
