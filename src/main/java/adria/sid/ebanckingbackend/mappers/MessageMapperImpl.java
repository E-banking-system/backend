package adria.sid.ebanckingbackend.mappers;

import adria.sid.ebanckingbackend.dtos.message.*;
import adria.sid.ebanckingbackend.ennumerations.MessageType;
import adria.sid.ebanckingbackend.entities.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MessageMapperImpl implements MessageMapper{

    @Override
    public MessageResDTO fromMessageToMessageResDTO(Message message) {
        MessageResDTO messageResDTO=new MessageResDTO();
        if(message.getReceiver() != null)
            messageResDTO.setReceiver(message.getReceiver().getId());
        if(message.getSender() != null)
            messageResDTO.setSender(message.getSender().getId());

        BeanUtils.copyProperties(message,messageResDTO);
        return messageResDTO;
    }

    @Override
    public MessageFileResDTO fromMessageToMessageFileResDTO(Message message) {
        MessageFileResDTO messageFileResDTO=new MessageFileResDTO();
        BeanUtils.copyProperties(message,messageFileResDTO);
        return messageFileResDTO;
    }

    @Override
    public Message fromClientMessageReqDTOToMessage(ClientMessageReqDTO messageReqDTO) {
        Message message=new Message();
        message.setId(UUID.randomUUID().toString());
        message.setLocalDateTime(new Date());
        message.setType(MessageType.CHAT);
        BeanUtils.copyProperties(messageReqDTO,message);
        return message;
    }

    @Override
    public Message fromClientMessageFileReqDTOToMessage(ClientMessageFileReqDTO clientMessageFileReqDTO) {
        Message message=new Message();
        message.setId(UUID.randomUUID().toString());
        BeanUtils.copyProperties(clientMessageFileReqDTO,message);
        return message;
    }

    @Override
    public Message fromBankerMessageReqDTOToMessage(BankerMessageReqDTO bankerMessageReqDTO) {
        System.out.println(bankerMessageReqDTO);
        Message message=new Message();
        message.setId(UUID.randomUUID().toString());
        message.setLocalDateTime(new Date());
        message.setType(MessageType.CHAT);
        BeanUtils.copyProperties(bankerMessageReqDTO,message);
        return message;
    }



    @Override
    public List<MessageResDTO> toMessageResDTOs(List<Message> messages) {
        return messages.stream()
                .map(this::fromMessageToMessageResDTO)
                .collect(Collectors.toList());
    }
}
