package adria.sid.ebanckingbackend.mappers;

import adria.sid.ebanckingbackend.dtos.virement.VirementPermanentReqDTO;
import adria.sid.ebanckingbackend.dtos.virement.VirementResDTO;
import adria.sid.ebanckingbackend.dtos.virement.VirementUnitReqDTO;
import adria.sid.ebanckingbackend.entities.Virement;
import adria.sid.ebanckingbackend.entities.VirementPermanant;
import adria.sid.ebanckingbackend.entities.VirementProgramme;
import adria.sid.ebanckingbackend.entities.VirementUnitaire;

import java.util.List;

public interface VirementMapper {
    VirementResDTO fromVirementToVirementResDTO(Virement virement);
    VirementUnitaire fromVirementReqDTOToVirementUnitaire(VirementUnitReqDTO virementUnitReqDTO);
    VirementPermanant fromVirementReqDTOToVirementPermanent(VirementUnitReqDTO virementUnitReqDTO);
    VirementPermanant fromVirementProgrammeToVirementPermanent(VirementProgramme virementProgramme);
    VirementProgramme fromVirementPermanentReqDTOToVirementProgramme(VirementPermanentReqDTO virementPermanentReqDTO);
    List<VirementResDTO> toVirementResDTO(List<Virement> virements);
}
