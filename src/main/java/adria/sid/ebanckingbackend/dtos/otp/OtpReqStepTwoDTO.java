package adria.sid.ebanckingbackend.dtos.otp;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpReqStepTwoDTO {
    @NotNull
    String userId;

    @NotNull
    String otpCode;
}
