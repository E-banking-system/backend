package adria.sid.ebanckingbackend.services.chat;

import adria.sid.ebanckingbackend.dtos.message.MessageResDTO;

import java.util.List;

public interface MessageService {
    List<MessageResDTO> getAllBeneficierMessages(String userId);
}
