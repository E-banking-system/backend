package adria.sid.ebanckingbackend.services;

import adria.sid.ebanckingbackend.dtos.ReqCreateAccountDTO;
import adria.sid.ebanckingbackend.ennumerations.EtatCompte;
import adria.sid.ebanckingbackend.entities.Compte;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class CompteService {

    public Compte createAccountForExistingUser(ReqCreateAccountDTO accountDTO){

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

        return newCompte;
    }
}
