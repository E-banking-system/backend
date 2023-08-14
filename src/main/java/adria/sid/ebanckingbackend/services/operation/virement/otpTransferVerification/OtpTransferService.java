package adria.sid.ebanckingbackend.services.operation.virement.otpTransferVerification;

import adria.sid.ebanckingbackend.dtos.otp.OtpReqStepOneDTO;
import adria.sid.ebanckingbackend.dtos.otp.OtpReqStepTwoDTO;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.exceptions.ExpiredTransferToken;
import adria.sid.ebanckingbackend.exceptions.InvalidTransferOtpCode;

import java.util.Optional;

public interface OtpTransferService {
    void createOtpTransferResetTokenForUser(OtpReqStepOneDTO otpReqStepOneDTO);
    String validateOtpTransferResetToken(OtpReqStepTwoDTO otpReqStepTwoDTO) throws InvalidTransferOtpCode, ExpiredTransferToken;
    Optional<UserEntity> findUserByOtpTransferToken(String otpTransfer);
}
