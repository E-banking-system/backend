package adria.sid.ebanckingbackend.repositories;

import adria.sid.ebanckingbackend.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

}

