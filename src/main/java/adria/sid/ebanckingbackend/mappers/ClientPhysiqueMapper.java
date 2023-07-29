package adria.sid.ebanckingbackend.mappers;

import adria.sid.ebanckingbackend.dtos.ClientPhysiqueDTO;
import adria.sid.ebanckingbackend.entities.UserEntity;

import java.util.List;

public interface ClientPhysiqueMapper {
    ClientPhysiqueDTO fromUserToClientPhysiqueDTO(UserEntity user);
    UserEntity fromClientPhysiqueToUser(ClientPhysiqueDTO clientPhysiqueDTO);
    List<ClientPhysiqueDTO> toClientsPhysiqueDTOs(List<UserEntity> users);
}
