package adria.sid.ebanckingbackend.mappers;

import adria.sid.ebanckingbackend.dtos.authentification.UserInfosResDTO;
import adria.sid.ebanckingbackend.entities.UserEntity;

public interface UserMapper {
    UserInfosResDTO fromUserToUserResDTO(UserEntity user);
}
