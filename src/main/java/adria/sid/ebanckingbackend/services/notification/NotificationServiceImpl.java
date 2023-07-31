package adria.sid.ebanckingbackend.services.notification;

import adria.sid.ebanckingbackend.dtos.notification.NotificationResDTO;
import adria.sid.ebanckingbackend.entities.Notification;
import adria.sid.ebanckingbackend.mappers.NotificationMapper;
import adria.sid.ebanckingbackend.repositories.NotificationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    final private NotificationRepository notificationRepository;
    final private NotificationMapper notificationMapper;

    @Override
    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
        log.info("Saved notification with ID: {}", notification.getId());
    }

    @Override
    public void modifierNotification(Notification notification) {
        log.info("Updated notification with ID: {}", notification.getId());
    }

    @Override
    public void supprimerNotification(String id) {
        notificationRepository.deleteById(id);
        log.info("Deleted notification with ID: {}", id);
    }

    @Override
    public Page<NotificationResDTO> getNotificationsByUserId(Pageable pageable, String userId) {
        List<Notification> notifications = notificationRepository.getNotificationsByUserId(userId);
        List<NotificationResDTO> notificationResDTOList = notifications.stream()
                .map(notificationMapper::fromNotificationToNotificationResDTO)
                .collect(Collectors.toList());

        log.info("Retrieved {} notifications for user ID: {}", notifications.size(), userId);

        return new PageImpl<>(notificationResDTOList, pageable, notifications.size());
    }
}
