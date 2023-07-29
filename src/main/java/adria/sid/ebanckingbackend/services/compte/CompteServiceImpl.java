package adria.sid.ebanckingbackend.services.compte;

import adria.sid.ebanckingbackend.dtos.compte.CompteReqDTO;
import adria.sid.ebanckingbackend.dtos.compte.CompteResDTO;
import adria.sid.ebanckingbackend.ennumerations.EtatCompte;
import adria.sid.ebanckingbackend.entities.Compte;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.mappers.CompteMapper;
import adria.sid.ebanckingbackend.repositories.CompteRepository;
import adria.sid.ebanckingbackend.repositories.UserRepository;
import adria.sid.ebanckingbackend.services.email.EmailSender;
    import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CompteServiceImpl implements CompteService {

    final private CompteRepository compteRepository;
    final private UserRepository userRepository;
    final private EmailSender emailSender;
    final private CompteMapper compteMapper;

    @Override
    @Transactional
    public void ajouterCompte(CompteReqDTO compteDTO) {
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

    @Override
    public Page<CompteResDTO> getAccounts(Pageable pageable) {
        Page<Compte> comptePage = compteRepository.findAll(pageable);
        return comptePage.map(compteMapper::fromCompteToCompteResDTO);
    }

    @Override
    public void activerCompte(String compteId) {
        Compte compte = compteRepository.getCompteById(compteId);
        if (compte != null) {
            compte.setEtatCompte(EtatCompte.ACTIVE);
            compteRepository.save(compte);
        } else {
            throw new IllegalArgumentException("Compte not found with the given ID");
        }
    }




    @Override
    public void blockCompte(String compteId) {
        Compte compte = compteRepository.getCompteById(compteId);
        if (compte != null) {
            compte.setEtatCompte(EtatCompte.BLOCKE);
            compteRepository.save(compte);
        } else {
            throw new IllegalArgumentException("Compte not found with the given ID");
        }
    }



    @Override
    public void suspendCompte(String compteId) {
        Compte compte = compteRepository.getCompteById(compteId);
        if (compte != null) {
            compte.setEtatCompte(EtatCompte.SUSPENDU);
            compteRepository.save(compte);
        } else {
            throw new IllegalArgumentException("Compte not found with the given ID");
        }
    }
}
