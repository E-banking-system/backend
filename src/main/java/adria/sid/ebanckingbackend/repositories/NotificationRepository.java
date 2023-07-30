package adria.sid.ebanckingbackend.repositories;

import adria.sid.ebanckingbackend.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,String> {
    List<Notification> getNotificationsByUserId(String userId);
}
