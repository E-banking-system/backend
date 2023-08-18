package adria.sid.ebanckingbackend.mappers;

import adria.sid.ebanckingbackend.dtos.message.BankerMessageReqDTO;
import adria.sid.ebanckingbackend.dtos.message.ClientMessageReqDTO;
import adria.sid.ebanckingbackend.dtos.message.MessageResDTO;
import adria.sid.ebanckingbackend.entities.Message;

import java.util.List;

public interface MessageMapper {
    List<MessageResDTO> toMessageResDTOs(List<Message> messages);
    MessageResDTO fromMessageToMessageResDTO(Message message);
    Message fromClientMessageReqDTOToMessage(ClientMessageReqDTO messageReqDTO);
    Message fromBankerMessageReqDTOToMessage(BankerMessageReqDTO bankerMessageReqDTO);
}
