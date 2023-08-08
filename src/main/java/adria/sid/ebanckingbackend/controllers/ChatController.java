package adria.sid.ebanckingbackend.controllers;

import adria.sid.ebanckingbackend.entities.Message;
import adria.sid.ebanckingbackend.repositories.MessageRepository;
import adria.sid.ebanckingbackend.repositories.UserRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public ChatController(
            SimpMessagingTemplate messagingTemplate,
            MessageRepository messageRepository,
            UserRepository userRepository
    ) {
        this.messagingTemplate = messagingTemplate;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload Message message) {
        message.setTimestamp(LocalDateTime.now());
        message.setSender(userRepository.findById(message.getSender().getId()).orElse(null));
        message.setReceiver(userRepository.findById(message.getReceiver().getId()).orElse(null));

        // Save the message in the database
        messageRepository.save(message);

        // Send the message to the user's specific destination
        messagingTemplate.convertAndSendToUser(message.getReceiver().getUsername(), "/queue/private", message);
    }
}
