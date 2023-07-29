package adria.sid.ebanckingbackend.mappers;

import adria.sid.ebanckingbackend.dtos.CompteDTO;
import adria.sid.ebanckingbackend.entities.Compte;

import java.util.List;

public interface CompteMapper {
    CompteDTO fromCompteToCompteDTO(Compte compte);
    Compte fromCompteDTOToCompte(CompteDTO compteDTO);
    List<CompteDTO> toComptesDTOs(List<Compte> comptes);

}
