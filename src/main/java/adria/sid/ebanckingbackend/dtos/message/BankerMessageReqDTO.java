package adria.sid.ebanckingbackend.dtos.message;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankerMessageReqDTO {
    @NotNull
    private String content;
    @NotNull
    private String senderId;
    @NotNull
    private String receiverId;
}
