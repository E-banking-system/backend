package adria.sid.ebanckingbackend.mappers;

import adria.sid.ebanckingbackend.dtos.ClientPhysiqueDTO;
import adria.sid.ebanckingbackend.ennumerations.EGender;
import adria.sid.ebanckingbackend.ennumerations.EPType;
import adria.sid.ebanckingbackend.ennumerations.ERole;
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
public class ClientPhysiqueMapperImpl implements ClientPhysiqueMapper{
    final private PasswordEncoder passwordEncoder;

    public ClientPhysiqueDTO fromUserToClientPhysiqueDTO(UserEntity user){
        ClientPhysiqueDTO clientPhysiqueDTO=new ClientPhysiqueDTO();
        BeanUtils.copyProperties(user,clientPhysiqueDTO);
        return  clientPhysiqueDTO;
    }

    public UserEntity fromClientPhysiqueToUser(ClientPhysiqueDTO clientPhysiqueDTO){
        UserEntity user=new UserEntity();
        user.setId(UUID.randomUUID().toString());
        user.setPersonneType(EPType.PHYSIQUE);
        user.setGender(EGender.valueOf(clientPhysiqueDTO.getGender()));
        user.setPassword(passwordEncoder.encode(clientPhysiqueDTO.getPassword()));
        user.setRole(ERole.CLIENT);
        BeanUtils.copyProperties(clientPhysiqueDTO,user);
        return  user;
    }

    public List<ClientPhysiqueDTO> toClientsPhysiqueDTOs(List<UserEntity> users) {
        return users.stream()
                .map(this::fromUserToClientPhysiqueDTO)
                .collect(Collectors.toList());
    }
}
