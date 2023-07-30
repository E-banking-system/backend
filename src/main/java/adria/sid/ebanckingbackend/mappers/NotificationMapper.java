package adria.sid.ebanckingbackend.mappers;

import adria.sid.ebanckingbackend.dtos.notification.NotificationResDTO;
import adria.sid.ebanckingbackend.entities.Notification;

import java.util.List;

public interface NotificationMapper {
    List<NotificationResDTO> toNotificationResDTOs(List<Notification> notifications);
    Notification fromNotificationResToNotification(NotificationResDTO notificationResDTO);
    NotificationResDTO fromNotificationToNotificationResDTO(Notification notification);
}
