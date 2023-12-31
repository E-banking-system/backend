package adria.sid.ebanckingbackend.mappers;

import adria.sid.ebanckingbackend.dtos.compte.CompteReqDTO;
import adria.sid.ebanckingbackend.dtos.compte.CompteResDTO;
import adria.sid.ebanckingbackend.entities.Compte;
import adria.sid.ebanckingbackend.utils.codeGenerators.CodeGenerator;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompteMapperImpl implements CompteMapper{
    final private CodeGenerator codeGenerator;

    @Override
    public CompteResDTO fromCompteToCompteResDTO(Compte compte) {
        CompteResDTO compteResDTO=new CompteResDTO();
        BeanUtils.copyProperties(compte,compteResDTO);
        return compteResDTO;
    }

    @Override
    public Compte fromCompteReqDTOToCompte(CompteReqDTO compteDTO) {
        Compte newCompte = new Compte();
        String numeroCompte = codeGenerator.numeroCompte();
        String rib = codeGenerator.generateRIBCode("874","985",numeroCompte,"55");
        String pin = codeGenerator.generatePinCode();

        // Manually set the ID for the Compte entity
        newCompte.setId(UUID.randomUUID().toString());

        newCompte.setRib(rib);
        newCompte.setNumCompte(String.valueOf(numeroCompte));
        newCompte.setDateCreation(new Date());

        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        // Add 4 years
        calendar.add(Calendar.YEAR, 4);
        // Get the new date after 4 years
        Date dateperemption = calendar.getTime();
        newCompte.setDatePeremption(dateperemption);

        newCompte.activerCompte();
        newCompte.setCodePIN(pin);

        // Now copy the other properties from compteDTO to newCompte
        BeanUtils.copyProperties(compteDTO, newCompte);
        return newCompte;
    }
}
