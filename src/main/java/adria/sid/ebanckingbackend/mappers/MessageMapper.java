package adria.sid.ebanckingbackend.mappers;

import adria.sid.ebanckingbackend.dtos.message.*;
import adria.sid.ebanckingbackend.entities.Message;

import java.util.List;

public interface MessageMapper {
    List<MessageResDTO> toMessageResDTOs(List<Message> messages);
    MessageResDTO fromMessageToMessageResDTO(Message message);
    MessageFileResDTO fromMessageToMessageFileResDTO(Message message);
    Message fromClientMessageReqDTOToMessage(ClientMessageReqDTO messageReqDTO);
    Message fromClientMessageFileReqDTOToMessage(ClientMessageFileReqDTO clientMessageFileReqDTO);
    Message fromBankerMessageReqDTOToMessage(BankerMessageReqDTO bankerMessageReqDTO);
}
