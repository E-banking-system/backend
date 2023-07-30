package adria.sid.ebanckingbackend.services.notification;

import adria.sid.ebanckingbackend.dtos.notification.NotificationResDTO;
import adria.sid.ebanckingbackend.entities.Compte;
import adria.sid.ebanckingbackend.entities.Notification;
import adria.sid.ebanckingbackend.mappers.NotificationMapper;
import adria.sid.ebanckingbackend.repositories.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService{
    final private NotificationRepository notificationRepository;
    final private NotificationMapper notificationMapper;

    @Override
    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    @Override
    public void modifierNotification(Notification notification) {

    }

    @Override
    public void supprimerNotification(String id) {
        notificationRepository.deleteById(id);
    }

    @Override
    public Page<NotificationResDTO> getNotificationsByUserId(Pageable pageable, String userId) {
        Page<Notification> notificationPage = notificationRepository.findAll(pageable);
        return notificationPage.map(notificationMapper::fromNotificationToNotificationResDTO);

    }
}
