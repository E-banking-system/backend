package adria.sid.ebanckingbackend.mappers;

import adria.sid.ebanckingbackend.dtos.message.MessageResDTO;
import adria.sid.ebanckingbackend.dtos.notification.NotificationResDTO;
import adria.sid.ebanckingbackend.entities.Message;
import adria.sid.ebanckingbackend.entities.Notification;
import adria.sid.ebanckingbackend.entities.VirementPermanant;
import adria.sid.ebanckingbackend.entities.VirementProgramme;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public List<MessageResDTO> toMessageResDTOs(List<Message> messages) {
        return messages.stream()
                .map(this::fromMessageToMessageResDTO)
                .collect(Collectors.toList());
    }
}
