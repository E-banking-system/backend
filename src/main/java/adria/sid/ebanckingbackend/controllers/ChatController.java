package adria.sid.ebanckingbackend.controllers;

import adria.sid.ebanckingbackend.dtos.message.BankerMessageReqDTO;
import adria.sid.ebanckingbackend.dtos.message.ClientMessageReqDTO;
import adria.sid.ebanckingbackend.dtos.message.MessageResDTO;
import adria.sid.ebanckingbackend.ennumerations.ERole;
import adria.sid.ebanckingbackend.ennumerations.MessageType;
import adria.sid.ebanckingbackend.entities.Message;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.exceptions.FileStorageException;
import adria.sid.ebanckingbackend.exceptions.IdUserIsNotValideException;
import adria.sid.ebanckingbackend.mappers.MessageMapper;
import adria.sid.ebanckingbackend.repositories.MessageRepository;
import adria.sid.ebanckingbackend.repositories.UserRepository;
import adria.sid.ebanckingbackend.services.chat.MessageService;
import adria.sid.ebanckingbackend.services.storage.FileStorageService;
import adria.sid.ebanckingbackend.services.storage.FileStorageServiceImpl;
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

import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Controller
@AllArgsConstructor
public class ChatController {
    final private MessageService messageService;
    final private MessageRepository messageRepository;
    private final FileStorageService fileStorageService;
    final private UserRepository userRepository;
    final private MessageMapper messageMapper;


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
            byte[] fileContent = Base64.getDecoder().decode(chatMessage.getContent());

            if (fileContent != null && fileContent.length <= 65536) {
                String fileName = chatMessage.getFileName(); // Extract file name from the message

                // Store the file content with the given file name
                fileStorageService.storeFile(fileContent, fileName);

                Message responseMessage = new Message();
                responseMessage.setId(UUID.randomUUID().toString());
                responseMessage.setContent(fileName);
                responseMessage.setType(MessageType.FILE);
                responseMessage.setFileType("FILE");
                responseMessage.setFileName(fileName);
                UserEntity sender=userRepository.findById(chatMessage.getSender().getId()).orElse(null);
                responseMessage.setSender(sender);
                UserEntity client=userRepository.findByRole(ERole.BANQUIER).get(0);
                responseMessage.setReceiver(client);
                responseMessage.setLocalDateTime(new Date());
                messageRepository.save(responseMessage);
                responseMessage.setFileData(fileContent);
                return ResponseEntity.ok(messageMapper.fromMessageToMessageFileResDTO(responseMessage)); // You can send a response message here if needed
            } else {
                return ResponseEntity.ok().body(null);
            }
        } catch (FileStorageException ex) {
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

    @MessageMapping("/banker.chat.sendFile")
    @SendTo("/topic/public")
    public ResponseEntity<?> bankerSendFile(@Payload Message chatMessage) {
        try {
            byte[] fileContent = Base64.getDecoder().decode(chatMessage.getContent());

            if (fileContent != null && fileContent.length <= 65536) {
                String fileName = chatMessage.getFileName(); // Extract file name from the message

                // Store the file content with the given file name
                fileStorageService.storeFile(fileContent, fileName);

                Message responseMessage = new Message();
                responseMessage.setId(UUID.randomUUID().toString());
                responseMessage.setContent(fileName);
                responseMessage.setType(MessageType.FILE);
                responseMessage.setFileType("FILE");
                responseMessage.setFileName(fileName);
                responseMessage.setSender(userRepository.findById(chatMessage.getSender().getId()).orElse(null));
                responseMessage.setReceiver(userRepository.findById(chatMessage.getReceiver().getId()).orElse(null));
                responseMessage.setLocalDateTime(new Date());
                messageRepository.save(responseMessage);
                responseMessage.setFileData(fileContent);
                return ResponseEntity.ok(messageMapper.fromMessageToMessageFileResDTO(responseMessage)); // You can send a response message here if needed
            } else {
                return ResponseEntity.ok().body(null);
            }
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
