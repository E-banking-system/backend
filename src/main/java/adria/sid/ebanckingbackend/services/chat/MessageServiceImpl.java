package adria.sid.ebanckingbackend.services.chat;

import adria.sid.ebanckingbackend.dtos.message.BankerMessageReqDTO;
import adria.sid.ebanckingbackend.dtos.message.ClientMessageReqDTO;
import adria.sid.ebanckingbackend.dtos.message.MessageResDTO;
import adria.sid.ebanckingbackend.ennumerations.ERole;
import adria.sid.ebanckingbackend.entities.Message;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.exceptions.IdUserIsNotValideException;
import adria.sid.ebanckingbackend.mappers.MessageMapper;
import adria.sid.ebanckingbackend.repositories.MessageRepository;
import adria.sid.ebanckingbackend.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService{
    final private MessageRepository messageRepository;
    final private UserRepository userRepository;
    final private MessageMapper messageMapper;

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

}
