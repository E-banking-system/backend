package adria.sid.ebanckingbackend.services.operation.virement.otpTransferVerification;

import adria.sid.ebanckingbackend.dtos.otp.OtpReqStepOneDTO;
import adria.sid.ebanckingbackend.dtos.otp.OtpReqStepTwoDTO;
import adria.sid.ebanckingbackend.entities.UserEntity;
import adria.sid.ebanckingbackend.exceptions.ExpiredTransferToken;
import adria.sid.ebanckingbackend.exceptions.IdUserIsNotValideException;
import adria.sid.ebanckingbackend.exceptions.InvalidTransferOtpCode;
import adria.sid.ebanckingbackend.repositories.UserRepository;
import adria.sid.ebanckingbackend.security.otpTransferToken.OtpTransferRepository;
import adria.sid.ebanckingbackend.security.otpTransferToken.OtpTransferToken;
import adria.sid.ebanckingbackend.security.passwordToken.MotdePasseTokenRepository;
import adria.sid.ebanckingbackend.security.passwordToken.MotdepasseToken;
import adria.sid.ebanckingbackend.services.email.EmailSender;
import adria.sid.ebanckingbackend.utils.codeGenerators.CodeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpTransferServiceImpl implements OtpTransferService {

    final private OtpTransferRepository otpTransferRepository;
    final private CodeGenerator codeGenerator;
    final private UserRepository userRepository;
    final private EmailSender emailSender;

    @Override
    public void createOtpTransferResetTokenForUser(OtpReqStepOneDTO otpReqStepOneDTO) {
        UserEntity userEntity=userRepository.findById(otpReqStepOneDTO.getUserId()).orElse(null);
        if(userEntity == null){
            log.warn("This use is not exists");
            throw new IdUserIsNotValideException("This user is not exists");
        }

        String token = codeGenerator.generateOtpVerificationCode();
        OtpTransferToken otpTransferToken = new OtpTransferToken(token, userEntity);
        otpTransferRepository.save(otpTransferToken);

        emailSender.sendUnitTransferVerificationByEmail(userEntity,token);

        log.info("Created otp tansfer reset token for user: {}", userEntity.getUsername());
    }

    @Override
    public String validateOtpTransferResetToken(OtpReqStepTwoDTO otpReqStepTwoDTO) throws InvalidTransferOtpCode, ExpiredTransferToken {
        UserEntity userEntity=userRepository.findById(otpReqStepTwoDTO.getUserId()).orElse(null);
        if(userEntity == null){
            log.warn("This user is not exist");
            throw new IdUserIsNotValideException("This user is not exist");
        }

        OtpTransferToken token = otpTransferRepository.findByToken(otpReqStepTwoDTO.getOtpCode());
        if (token == null) {
            log.warn("Invalid otp transfer token: {}", otpReqStepTwoDTO.getOtpCode());
            throw new InvalidTransferOtpCode("This verification code is not valid");
        }

        UserEntity user = token.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
            log.warn("Otp tansfer token has expired for user: {}", user.getUsername());
            throw new ExpiredTransferToken("Otp tansfer token has expired for user");
        }

        try {
            token.setVerified(true);
            otpTransferRepository.save(token);
            log.info("This otp verification token entity is verified with success");
        } catch (Exception e){
            log.warn("Cannot update otp verification token entity");
            throw new Error("Cannot verify otp verification token entity");
        }

        log.info("Valid otp transfer reset token for user: {}", user.getUsername());
        return "valid";
    }

    @Override
    public Optional<UserEntity> findUserByOtpTransferToken(String otpTransferToken) {
        OtpTransferToken token = otpTransferRepository.findByToken(otpTransferToken);
        UserEntity user = (token != null) ? token.getUser() : null;
        if (user != null) {
            log.info("Found user with otp transfer reset token: {}", user.getUsername());
        } else {
            log.info("No user found with otp transfer token : {}", otpTransferToken);
        }
        return Optional.ofNullable(user);
    }
}