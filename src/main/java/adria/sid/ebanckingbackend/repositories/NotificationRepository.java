package adria.sid.ebanckingbackend.repositories;

import adria.sid.ebanckingbackend.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,String> {
    List<Notification> getNotificationsByUserId(String userId);

    @Query(value = "SELECT COUNT(n) FROM Notification n WHERE n.user.id = :userId")
    int getNbrNotificationsByUserId(@Param("userId") String userId);

    @Query(value = "SELECT COUNT(m) FROM Message m WHERE m.sender.id = :userId or m.receiver.id = :userId")
    int getNbrMessagesByUserId(@Param("userId") String userId);
}
