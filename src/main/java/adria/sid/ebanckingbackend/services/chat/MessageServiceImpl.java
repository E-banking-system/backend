package adria.sid.ebanckingbackend.services.chat;

import adria.sid.ebanckingbackend.dtos.message.MessageResDTO;
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

    public List<MessageResDTO> getAllBeneficierMessages(String userId){
        UserEntity userEntity=userRepository.findById(userId).orElse(null);

        if(userEntity == null){
            throw new IdUserIsNotValideException("This user is not exists");
        }
        List<Message> messages=messageRepository.getMessagesBySenderIdOrReceiverId(userId,userId);
        return messageMapper.toMessageResDTOs(messages);
    }
}
