package adria.sid.ebanckingbackend.controllers;

import adria.sid.ebanckingbackend.entities.Message;
import adria.sid.ebanckingbackend.repositories.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@AllArgsConstructor
@CrossOrigin("*")
public class ChatController {
    final private MessageRepository messageRepository;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public Message sendMessage(
            @Payload Message chatMessage
    ) {
        chatMessage.setId(UUID.randomUUID().toString());
        chatMessage.setLocalDateTime(new Date());
        messageRepository.save(chatMessage);
        System.out.println(messageRepository.findAll().size() + " messages are saved now");
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public Message addUser(
            @Payload Message chatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        chatMessage.setId(UUID.randomUUID().toString());
        messageRepository.save(chatMessage);
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        System.out.println(messageRepository.findAll().size() + " messages are saved now");
        return chatMessage;
    }

    @GetMapping("/messages")
    @ResponseBody
    public List<Message> getMessages(){
        return messageRepository.findAll();
    }
}
