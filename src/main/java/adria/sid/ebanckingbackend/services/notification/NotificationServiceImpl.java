package adria.sid.ebanckingbackend.services.notification;

import adria.sid.ebanckingbackend.dtos.notification.NotificationResDTO;
import adria.sid.ebanckingbackend.entities.Notification;
import adria.sid.ebanckingbackend.mappers.NotificationMapper;
import adria.sid.ebanckingbackend.repositories.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        List<Notification> notifications = notificationRepository.getNotificationsByUserId(userId);
        List<NotificationResDTO> notificationResDTOList = notifications.stream()
                .map(notificationMapper::fromNotificationToNotificationResDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(notificationResDTOList, pageable, notifications.size());
    }
}
