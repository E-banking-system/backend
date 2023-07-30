package adria.sid.ebanckingbackend.mappers;

import adria.sid.ebanckingbackend.dtos.compte.CompteReqDTO;
import adria.sid.ebanckingbackend.dtos.compte.CompteResDTO;
import adria.sid.ebanckingbackend.entities.Compte;

import java.util.List;

public interface CompteMapper {
    CompteReqDTO fromCompteToCompteReqDTO(Compte compte);
    CompteResDTO fromCompteToCompteResDTO(Compte compte);
    Compte fromCompteDTOToCompte(CompteReqDTO compteDTO);
    List<CompteResDTO> toComptesResDTOs(List<Compte> comptes);
}
