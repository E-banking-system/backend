package adria.sid.ebanckingbackend.repositories;

import adria.sid.ebanckingbackend.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification,String> {
}
