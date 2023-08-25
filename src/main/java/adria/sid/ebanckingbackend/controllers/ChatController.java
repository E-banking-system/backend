package adria.sid.ebanckingbackend.controllers;

import adria.sid.ebanckingbackend.dtos.message.BankerMessageReqDTO;
import adria.sid.ebanckingbackend.dtos.message.ClientMessageReqDTO;
import adria.sid.ebanckingbackend.dtos.message.MessageResDTO;
import adria.sid.ebanckingbackend.entities.Message;
import adria.sid.ebanckingbackend.exceptions.FileStorageException;
import adria.sid.ebanckingbackend.exceptions.IdUserIsNotValideException;
import adria.sid.ebanckingbackend.services.chat.MessageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@AllArgsConstructor
public class ChatController {
    final private MessageService messageService;

    @MessageMapping("/client.chat.sendMessage")
    @SendTo("/topic/public")
    public MessageResDTO clientSendMessage(
            @Payload @Valid ClientMessageReqDTO messageReqDTO
    ) {
        return messageService.clientSendMessage(messageReqDTO);
    }

    @MessageMapping("/banker.chat.sendMessage")
    @SendTo("/topic/public")
    public MessageResDTO bankerSendMessage(
            @Payload @Valid BankerMessageReqDTO bankerMessageReqDTO
    ) {
        return messageService.bankerSendMessage(bankerMessageReqDTO);
    }

    @GetMapping("/messages")
    @ResponseBody
    public ResponseEntity<?> getMessages(@RequestParam @NotNull String userId){
        try {
            return ResponseEntity.ok(messageService.getAllBeneficierMessages(userId));
        } catch (IdUserIsNotValideException e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @MessageMapping("/client.chat.sendFile")
    @SendTo("/topic/public")
    public ResponseEntity<?> clientSendFile(@Payload Message chatMessage) {
        try {
            return ResponseEntity.ok().body(messageService.clientSendFileMessage(chatMessage));
        } catch (FileStorageException ex) {
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

    @MessageMapping("/banker.chat.sendFile")
    @SendTo("/topic/public")
    public ResponseEntity<?> bankerSendFile(@Payload Message chatMessage) {
        try {
            return ResponseEntity.ok().body(messageService.bankerSendFileMessage(chatMessage));
        } catch (FileStorageException ex) {
           return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

    @GetMapping("/BankerClientMessages")
    @ResponseBody
    public ResponseEntity<?> getMessagesBetweenBankerAndClient(@RequestParam @NotNull String userId, @RequestParam @NotNull String receiverId){
        try {
            return ResponseEntity.ok(messageService.getConvoMessages(userId,receiverId));
        } catch (IdUserIsNotValideException e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
