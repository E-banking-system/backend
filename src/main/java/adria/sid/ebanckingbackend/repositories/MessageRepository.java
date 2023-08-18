package adria.sid.ebanckingbackend.repositories;

import adria.sid.ebanckingbackend.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, String> {
    List<Message> getMessagesBySenderIdOrReceiverId(String senderId,String receiverId);
    List<Message> getMessagesBySenderIdAndReceiverId(String senderId,String receiverId);
}

