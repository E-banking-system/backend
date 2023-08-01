package adria.sid.ebanckingbackend.mappers;

import adria.sid.ebanckingbackend.dtos.beneficier.BeneficierResDTO;
import adria.sid.ebanckingbackend.dtos.client.ClientMoraleDTO;
import adria.sid.ebanckingbackend.ennumerations.EPType;
import adria.sid.ebanckingbackend.ennumerations.ERole;
import adria.sid.ebanckingbackend.entities.Beneficier;
import adria.sid.ebanckingbackend.entities.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BeneficierMapperImpl implements BeneficierMapper{
    final private PasswordEncoder passwordEncoder;
    @Override
    public BeneficierResDTO fromBeneficierToBeneficierResDTO(Beneficier beneficier) {
        BeneficierResDTO beneficierResDTO=new BeneficierResDTO();
        BeanUtils.copyProperties(beneficier,beneficierResDTO);
        return  beneficierResDTO;
    }

    @Override
    public List<BeneficierResDTO> toBeneficierResDTOs(List<Beneficier> beneficiers) {
        return beneficiers.stream()
                .map(this::fromBeneficierToBeneficierResDTO)
                .collect(Collectors.toList());
    }
}
