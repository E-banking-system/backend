package adria.sid.ebanckingbackend.services.chat;

import adria.sid.ebanckingbackend.dtos.message.*;
import adria.sid.ebanckingbackend.entities.Message;

import java.util.List;

public interface MessageService {
    List<MessageResDTO> getAllBeneficierMessages(String userId);
    MessageResDTO clientSendMessage(ClientMessageReqDTO clientMessageReqDTO);
    MessageResDTO bankerSendMessage(BankerMessageReqDTO bankerMessageReqDTO);
    MessageFileResDTO clientSendFileMessage(Message message);
    MessageFileResDTO bankerSendFileMessage(Message message);
    List<MessageResDTO> getConvoMessages(String userId, String receiverId);;
}
