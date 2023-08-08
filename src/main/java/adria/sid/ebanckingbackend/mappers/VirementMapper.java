package adria.sid.ebanckingbackend.mappers;

import adria.sid.ebanckingbackend.entities.VirementPermanant;
import adria.sid.ebanckingbackend.entities.VirementProgramme;

public interface VirementMapper {
    VirementPermanant fromVirementProgrammeToVirementPermanent(VirementProgramme virementProgramme);
}
