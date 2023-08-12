package adria.sid.ebanckingbackend.mappers;

import adria.sid.ebanckingbackend.dtos.client.ClientPhysiqueDTO;
import adria.sid.ebanckingbackend.dtos.notification.NotificationResDTO;
import adria.sid.ebanckingbackend.ennumerations.EGender;
import adria.sid.ebanckingbackend.ennumerations.EPType;
import adria.sid.ebanckingbackend.ennumerations.ERole;
import adria.sid.ebanckingbackend.entities.Notification;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.services.notification.NotificationService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NotificationMapperImpl implements NotificationMapper {
    @Override
    public NotificationResDTO fromNotificationToNotificationResDTO(Notification notification){
        NotificationResDTO notificationResDTO=new NotificationResDTO();
        BeanUtils.copyProperties(notification,notificationResDTO);
        return  notificationResDTO;
    }

    @Override
    public List<NotificationResDTO> toNotificationResDTOs(List<Notification> notifications) {
        return notifications.stream()
                .map(this::fromNotificationToNotificationResDTO)
                .collect(Collectors.toList());
    }
}

