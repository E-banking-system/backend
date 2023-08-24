package adria.sid.ebanckingbackend.services.chat;

import adria.sid.ebanckingbackend.dtos.message.*;

import java.util.List;

public interface MessageService {
    List<MessageResDTO> getAllBeneficierMessages(String userId);
    MessageResDTO clientSendMessage(ClientMessageReqDTO clientMessageReqDTO);
    MessageResDTO bankerSendMessage(BankerMessageReqDTO bankerMessageReqDTO);
    List<MessageResDTO> getConvoMessages(String userId, String receiverId);;
}
