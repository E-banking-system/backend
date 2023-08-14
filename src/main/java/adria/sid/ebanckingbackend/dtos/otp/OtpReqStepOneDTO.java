package adria.sid.ebanckingbackend.dtos.otp;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpReqStepOneDTO {
    @NotNull
    String userId;
}
