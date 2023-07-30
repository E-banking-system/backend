package adria.sid.ebanckingbackend.services.notification;

import adria.sid.ebanckingbackend.dtos.notification.NotificationResDTO;
import adria.sid.ebanckingbackend.entities.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {
    void saveNotification(Notification notification);
    void modifierNotification(Notification notification);
    void supprimerNotification(String id);
    Page<NotificationResDTO> getNotificationsByUserId(Pageable pageable, String userId);
}
