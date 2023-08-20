package adria.sid.ebanckingbackend.services.notification;

import adria.sid.ebanckingbackend.dtos.notification.NotificationResDTO;
import adria.sid.ebanckingbackend.entities.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    Page<NotificationResDTO> getNotificationsByUserId(Pageable pageable, String userId);
    int getNbrNotificationsByUserId(String userId);
    int getNbrMessagesByUserId(String userId);
}
