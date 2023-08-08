package adria.sid.ebanckingbackend.mappers;

import adria.sid.ebanckingbackend.entities.VirementPermanant;
import adria.sid.ebanckingbackend.entities.VirementProgramme;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class VirementMapperImpl implements VirementMapper{
    @Override
    public VirementPermanant fromVirementProgrammeToVirementPermanent(VirementProgramme virementProgramme){
        VirementPermanant virementPermanant=new VirementPermanant();
        BeanUtils.copyProperties(virementProgramme,virementPermanant);
        return  virementPermanant;
    }
}
