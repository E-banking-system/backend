package adria.sid.ebanckingbackend.mappers;

import adria.sid.ebanckingbackend.dtos.compte.CompteReqDTO;
import adria.sid.ebanckingbackend.dtos.compte.CompteResDTO;
import adria.sid.ebanckingbackend.entities.Compte;

import java.util.List;

public interface CompteMapper {
    CompteResDTO fromCompteToCompteResDTO(Compte compte);
    Compte fromCompteReqDTOToCompte(CompteReqDTO compteDTO);
}
