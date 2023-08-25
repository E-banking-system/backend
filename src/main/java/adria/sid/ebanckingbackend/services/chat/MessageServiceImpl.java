package adria.sid.ebanckingbackend.services.chat;

import adria.sid.ebanckingbackend.dtos.message.*;
import adria.sid.ebanckingbackend.ennumerations.ERole;
import adria.sid.ebanckingbackend.ennumerations.MessageType;
import adria.sid.ebanckingbackend.entities.Message;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.exceptions.IdUserIsNotValideException;
import adria.sid.ebanckingbackend.mappers.MessageMapper;
import adria.sid.ebanckingbackend.repositories.MessageRepository;
import adria.sid.ebanckingbackend.repositories.UserRepository;
import adria.sid.ebanckingbackend.services.storage.FileStorageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService{
    final private MessageRepository messageRepository;
    final private UserRepository userRepository;
    final private MessageMapper messageMapper;
    final private FileStorageService fileStorageService;

    @Override
    public List<MessageResDTO> getConvoMessages(String userId, String receiverId){
        UserEntity userEntity=userRepository.findById(userId).orElse(null);

        if(userEntity == null){
            throw new IdUserIsNotValideException("This user is not exists");
        }
        List<Message> messages=messageRepository.getMessagesBySenderIdAndReceiverId(userId,receiverId);
        return messageMapper.toMessageResDTOs(messages);
    }

    @Override
    public List<MessageResDTO> getAllBeneficierMessages(String userId){
        UserEntity userEntity=userRepository.findById(userId).orElse(null);

        if(userEntity == null){
            throw new IdUserIsNotValideException("This user is not exists");
        }
        List<Message> messages=messageRepository.getMessagesBySenderIdOrReceiverId(userId,userId);
        return messageMapper.toMessageResDTOs(messages);
    }

    @Override
    public MessageResDTO clientSendMessage(ClientMessageReqDTO clientMessageReqDTO){
        // Get the saved sender UserEntity or save it if not present
        UserEntity sender = userRepository.findById(clientMessageReqDTO.getSenderId()).orElse(null);
        if(sender == null){
            throw new IdUserIsNotValideException("Sender id is not valid");
        }

        Message message=messageMapper.fromClientMessageReqDTOToMessage(clientMessageReqDTO);
        message.setSender(sender);

        UserEntity receiver=userRepository.findByRole(ERole.BANQUIER).get(0);
        message.setReceiver(receiver);

        messageRepository.save(message);
        return messageMapper.fromMessageToMessageResDTO(message);
    }

    @Override
    public MessageResDTO bankerSendMessage(BankerMessageReqDTO bankerMessageReqDTO){
        // Get the saved sender UserEntity or save it if not present
        UserEntity sender = userRepository.findById(bankerMessageReqDTO.getSenderId()).orElse(null);
        if(sender == null){
            throw new IdUserIsNotValideException("Sender id is not valid");
        }

        // Get the saved sender UserEntity or save it if not present
        UserEntity receiver = userRepository.findById(bankerMessageReqDTO.getReceiverId()).orElse(null);
        if(receiver == null){
            throw new IdUserIsNotValideException("Receiver id is not valid");
        }

        Message message = messageMapper.fromBankerMessageReqDTOToMessage(bankerMessageReqDTO);

        message.setSender(sender);
        message.setReceiver(receiver);
        messageRepository.save(message); // Save the message again with the updated sender reference

        return messageMapper.fromMessageToMessageResDTO(message);
    }

    @Override
    public MessageFileResDTO clientSendFileMessage(Message message) {
        byte[] fileContent = Base64.getDecoder().decode(message.getContent());

        if (fileContent != null && fileContent.length <= 65536) {
            String fileName = message.getFileName(); // Extract file name from the message

            // Store the file content with the given file name
            fileStorageService.storeFile(fileContent, fileName);

            Message responseMessage = new Message();
            responseMessage.setId(UUID.randomUUID().toString());
            responseMessage.setContent(fileName);
            responseMessage.setType(MessageType.FILE);
            responseMessage.setFileType("FILE");
            responseMessage.setFileName(fileName);
            UserEntity sender=userRepository.findById(message.getSender().getId()).orElse(null);
            responseMessage.setSender(sender);
            UserEntity client=userRepository.findByRole(ERole.BANQUIER).get(0);
            responseMessage.setReceiver(client);
            responseMessage.setLocalDateTime(new Date());
            messageRepository.save(responseMessage);
            responseMessage.setFileData(fileContent);
            return messageMapper.fromMessageToMessageFileResDTO(responseMessage); // You can send a response message here if needed
        } else {
            return null;
        }
    }

    @Override
    public MessageFileResDTO bankerSendFileMessage(Message message) {
        byte[] fileContent = Base64.getDecoder().decode(message.getContent());

        if (fileContent != null && fileContent.length <= 65536) {
            String fileName = message.getFileName(); // Extract file name from the message

            // Store the file content with the given file name
            fileStorageService.storeFile(fileContent, fileName);

            Message responseMessage = new Message();
            responseMessage.setId(UUID.randomUUID().toString());
            responseMessage.setContent(fileName);
            responseMessage.setType(MessageType.FILE);
            responseMessage.setFileType("FILE");
            responseMessage.setFileName(fileName);
            responseMessage.setSender(userRepository.findById(message.getSender().getId()).orElse(null));
            responseMessage.setReceiver(userRepository.findById(message.getReceiver().getId()).orElse(null));
            responseMessage.setLocalDateTime(new Date());
            messageRepository.save(responseMessage);
            responseMessage.setFileData(fileContent);
            return messageMapper.fromMessageToMessageFileResDTO(responseMessage); // You can send a response message here if needed
        } else {
            return null;
        }
    }


}
