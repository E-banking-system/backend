package adria.sid.ebanckingbackend.repositories;

import adria.sid.ebanckingbackend.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, String> {
    List<Message> getMessagesBySenderIdOrReceiverId(String senderId,String receiverId);

    @Query(value = "SELECT m FROM Message m " +
            "WHERE (m.sender.id = :userId OR m.receiver.id = :userId) " +
            "AND (m.receiver.id = :receiverId OR m.sender.id = :receiverId)")
    List<Message> getMessagesBySenderIdAndReceiverId(@Param("userId") String userId, @Param("receiverId") String receiverId);
}

