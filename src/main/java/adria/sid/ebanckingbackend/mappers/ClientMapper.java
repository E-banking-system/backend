package adria.sid.ebanckingbackend.mappers;

import adria.sid.ebanckingbackend.dtos.client.ClientResDTO;
import adria.sid.ebanckingbackend.entities.UserEntity;

import java.util.List;

public interface ClientMapper {
    ClientResDTO fromUserToClientResDTO(UserEntity user);
}
