package adria.sid.ebanckingbackend.mappers;

import adria.sid.ebanckingbackend.dtos.beneficier.BeneficierReqDTO;
import adria.sid.ebanckingbackend.dtos.beneficier.BeneficierResDTO;
import adria.sid.ebanckingbackend.entities.Beneficier;

import java.util.List;

public interface BeneficierMapper {
    BeneficierResDTO fromBeneficierToBeneficierResDTO(Beneficier beneficier);
    Beneficier fromBeneficierReqDTOToBeneficier(BeneficierReqDTO beneficierReqDTO);
    List<BeneficierResDTO> toBeneficierResDTOs(List<Beneficier> beneficiers);
}
