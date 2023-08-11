package adria.sid.ebanckingbackend.mappers;

import adria.sid.ebanckingbackend.dtos.beneficier.BeneficierReqDTO;
import adria.sid.ebanckingbackend.dtos.beneficier.BeneficierResDTO;
import adria.sid.ebanckingbackend.entities.Beneficier;
import adria.sid.ebanckingbackend.entities.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BeneficierMapperImpl implements BeneficierMapper{
    @Override
    public BeneficierResDTO fromBeneficierToBeneficierResDTO(Beneficier beneficier) {
        BeneficierResDTO beneficierResDTO=new BeneficierResDTO();
        BeanUtils.copyProperties(beneficier,beneficierResDTO);
        beneficierResDTO.setParent_user_id(beneficier.getParent_user().getId());
        beneficierResDTO.setUser_manager_id(beneficier.getUser().getId());
        beneficierResDTO.setNom(beneficier.getParent_user().getNom());
        beneficierResDTO.setPrenom(beneficier.getParent_user().getPrenom());
        return  beneficierResDTO;
    }

    @Override
    public Beneficier fromBeneficierReqDTOToBeneficier(BeneficierReqDTO beneficierReqDTO) {
        Beneficier beneficier=new Beneficier();
        beneficier.setBeneficier_id(UUID.randomUUID().toString());
        UserEntity client=new UserEntity();
        client.setId(beneficierReqDTO.getClientId());
        beneficier.setUser(client);
        BeanUtils.copyProperties(beneficierReqDTO,beneficier);
        return beneficier;
    }

    @Override
    public List<BeneficierResDTO> toBeneficierResDTOs(List<Beneficier> beneficiers) {
        return beneficiers.stream()
                .map(this::fromBeneficierToBeneficierResDTO)
                .collect(Collectors.toList());
    }
}
