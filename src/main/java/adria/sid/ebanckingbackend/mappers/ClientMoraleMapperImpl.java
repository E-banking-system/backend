package adria.sid.ebanckingbackend.mappers;

import adria.sid.ebanckingbackend.dtos.ClientMoraleDTO;
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
public class ClientMoraleMapperImpl implements ClientMoraleMapper{
    final private PasswordEncoder passwordEncoder;
    @Override
    public ClientMoraleDTO fromUserToClientMoraleDTO(UserEntity user) {
        ClientMoraleDTO clientMoraleDTO=new ClientMoraleDTO();
        BeanUtils.copyProperties(user,clientMoraleDTO);
        return  clientMoraleDTO;
    }

    @Override
    public UserEntity fromClientMoraleToUser(ClientMoraleDTO clientMoraleDTO) {
        UserEntity user=new UserEntity();
        user.setId(UUID.randomUUID().toString());
        user.setPersonneType(EPType.MORALE);
        user.setPassword(passwordEncoder.encode(clientMoraleDTO.getPassword()));
        user.setRole(ERole.CLIENT);
        BeanUtils.copyProperties(clientMoraleDTO,user);
        return  user;
    }

    @Override
    public List<ClientMoraleDTO> toClientsMoraleDTOs(List<UserEntity> users) {
        return users.stream()
                .map(this::fromUserToClientMoraleDTO)
                .collect(Collectors.toList());
    }
}
