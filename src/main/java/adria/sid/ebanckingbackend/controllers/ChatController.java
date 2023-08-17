package adria.sid.ebanckingbackend.controllers;

import adria.sid.ebanckingbackend.dtos.message.MessageResDTO;
import adria.sid.ebanckingbackend.entities.Message;
import adria.sid.ebanckingbackend.exceptions.IdUserIsNotValideException;
import adria.sid.ebanckingbackend.mappers.MessageMapper;
import adria.sid.ebanckingbackend.repositories.MessageRepository;
import adria.sid.ebanckingbackend.services.chat.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@AllArgsConstructor
public class ChatController {
    final private MessageRepository messageRepository;
    final private MessageMapper messageMapper;
    final private MessageService messageService;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public MessageResDTO sendMessage(
            @Payload Message chatMessage
    ) {
        chatMessage.setId(UUID.randomUUID().toString());
        chatMessage.setLocalDateTime(new Date());
        messageRepository.save(chatMessage);
        System.out.println(messageRepository.findAll().size() + " messages are saved now");
        return messageMapper.fromMessageToMessageResDTO(chatMessage);
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public MessageResDTO addUser(
            @Payload Message chatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        chatMessage.setId(UUID.randomUUID().toString());
        messageRepository.save(chatMessage);
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        System.out.println(messageRepository.findAll().size() + " messages are saved now");
        return messageMapper.fromMessageToMessageResDTO(chatMessage);
    }

    @GetMapping("/messages")
    @ResponseBody
    public ResponseEntity<?> getMessages(@RequestParam String userId){
        try {
            return ResponseEntity.ok(messageService.getAllBeneficierMessages(userId));
        } catch (IdUserIsNotValideException e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
