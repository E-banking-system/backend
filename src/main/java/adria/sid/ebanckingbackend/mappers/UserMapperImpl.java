package adria.sid.ebanckingbackend.mappers;

import adria.sid.ebanckingbackend.dtos.authentification.UserInfosResDTO;
import adria.sid.ebanckingbackend.entities.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserMapperImpl implements UserMapper{
    @Override
    public UserInfosResDTO fromUserToUserResDTO(UserEntity user){
        UserInfosResDTO userInfosResDTO=new UserInfosResDTO();
        BeanUtils.copyProperties(user,userInfosResDTO);
        return  userInfosResDTO;
    }
}
