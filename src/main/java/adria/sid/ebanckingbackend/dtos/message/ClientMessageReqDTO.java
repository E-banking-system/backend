package adria.sid.ebanckingbackend.dtos.message;

import adria.sid.ebanckingbackend.entities.UserEntity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientMessageReqDTO {
    @NotNull
    private String content;
    @NotNull
    private String senderId;
}
