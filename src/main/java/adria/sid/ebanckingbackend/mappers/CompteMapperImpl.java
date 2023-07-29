package adria.sid.ebanckingbackend.mappers;

import adria.sid.ebanckingbackend.dtos.CompteDTO;
import adria.sid.ebanckingbackend.entities.Compte;
import adria.sid.ebanckingbackend.utils.codeGenerators.CodeGenerator;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompteMapperImpl implements CompteMapper{
    final private CodeGenerator codeGenerator;
    @Override
    public CompteDTO fromCompteToCompteDTO(Compte compte) {
        CompteDTO compteDTO=new CompteDTO();
        BeanUtils.copyProperties(compte,compteDTO);
        return  compteDTO;
    }

    @Override
    public Compte fromCompteDTOToCompte(CompteDTO compteDTO) {
        Compte newCompte = new Compte();
        String rib = codeGenerator.generateRIBCode();
        String pin = codeGenerator.generatePinCode();
        Long numeroCompte = codeGenerator.numeroCompte();

        // Manually set the ID for the Compte entity
        newCompte.setId(UUID.randomUUID().toString());

        newCompte.setRIB(rib);
        newCompte.setNumCompte(numeroCompte);
        newCompte.setDateCreation(new Date());
        newCompte.activerCompte();
        newCompte.setCodePIN(pin);

        // Now copy the other properties from compteDTO to newCompte
        BeanUtils.copyProperties(compteDTO, newCompte);
        return newCompte;
    }


    @Override
    public List<CompteDTO> toComptesDTOs(List<Compte> comptes) {
        return comptes.stream()
                .map(this::fromCompteToCompteDTO)
                .collect(Collectors.toList());
    }
}
