package adria.sid.ebanckingbackend.mappers;

import adria.sid.ebanckingbackend.dtos.client.ClientMoraleDTO;
import adria.sid.ebanckingbackend.entities.UserEntity;

import java.util.List;

public interface ClientMoraleMapper {
    ClientMoraleDTO fromUserToClientMoraleDTO(UserEntity user);
    UserEntity fromClientMoraleToUser(ClientMoraleDTO clientMoraleDTO);
}
