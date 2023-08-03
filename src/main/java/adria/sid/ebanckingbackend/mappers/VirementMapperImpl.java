package adria.sid.ebanckingbackend.mappers;

import adria.sid.ebanckingbackend.dtos.virement.VirementPermanentReqDTO;
import adria.sid.ebanckingbackend.dtos.virement.VirementResDTO;
import adria.sid.ebanckingbackend.dtos.virement.VirementUnitReqDTO;
import adria.sid.ebanckingbackend.entities.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VirementMapperImpl implements VirementMapper{

    @Override
    public VirementResDTO fromVirementToVirementResDTO(Virement virement){
        VirementResDTO virementResDTO=new VirementResDTO();
        BeanUtils.copyProperties(virement,virementResDTO);
        return virementResDTO;
    }

    @Override
    public VirementUnitaire fromVirementReqDTOToVirementUnitaire(VirementUnitReqDTO virementUnitReqDTO){
        VirementUnitaire virementUnitaire=new VirementUnitaire();
        virementUnitaire.setId(UUID.randomUUID().toString());
        virementUnitaire.setDateOperation(new Date());
        virementUnitaire.setEstUnitaire(true);
        BeanUtils.copyProperties(virementUnitReqDTO,virementUnitaire);
        return virementUnitaire;
    }

    @Override
    public VirementPermanant fromVirementReqDTOToVirementPermanent(VirementUnitReqDTO virementUnitReqDTO){
        VirementPermanant virementPermanant=new VirementPermanant();
        virementPermanant.setId(UUID.randomUUID().toString());
        BeanUtils.copyProperties(virementUnitReqDTO,virementPermanant);
        return virementPermanant;
    }

    @Override
    public VirementProgramme fromVirementPermanentReqDTOToVirementProgramme(VirementPermanentReqDTO virementPermanentReqDTO){
        VirementProgramme virementProgramme=new VirementProgramme();
        virementProgramme.setId(UUID.randomUUID().toString());
        BeanUtils.copyProperties(virementPermanentReqDTO,virementProgramme);
        return virementProgramme;
    }

    @Override
    public VirementPermanant fromVirementProgrammeToVirementPermanent(VirementProgramme virementProgramme){
        VirementPermanant virementPermanant=new VirementPermanant();
        virementPermanant.setId(UUID.randomUUID().toString());
        BeanUtils.copyProperties(virementProgramme,virementPermanant);
        return virementPermanant;
    }

    @Override
    public List<VirementResDTO> toVirementResDTO(List<Virement> virements) {
        return virements.stream()
                .map(this::fromVirementToVirementResDTO)
                .collect(Collectors.toList());
    }
}
