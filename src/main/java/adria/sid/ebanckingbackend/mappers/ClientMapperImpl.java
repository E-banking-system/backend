package adria.sid.ebanckingbackend.mappers;

import adria.sid.ebanckingbackend.dtos.client.ClientResDTO;
import adria.sid.ebanckingbackend.entities.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ClientMapperImpl implements ClientMapper{
    @Override
    public ClientResDTO fromUserToClientResDTO(UserEntity user) {
        ClientResDTO clientResDTO=new ClientResDTO();
        clientResDTO.setEmail(user.getEmail());
        BeanUtils.copyProperties(user,clientResDTO);
        return  clientResDTO;
    }

    @Override
    public List<ClientResDTO> toClientResDTOs(List<UserEntity> users) {
        return users.stream()
                .map(this::fromUserToClientResDTO)
                .collect(Collectors.toList());
    }
}
