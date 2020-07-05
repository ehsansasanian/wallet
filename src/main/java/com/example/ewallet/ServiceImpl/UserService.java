package com.example.ewallet.ServiceImpl;

import com.example.ewallet.base.Hash;
import com.example.ewallet.domain.TransactionLogs;
import com.example.ewallet.domain.UserAcc;
import com.example.ewallet.domain.UserPic;
import com.example.ewallet.domain.UserSecurityCode;
import com.example.ewallet.enums.LoginResponse;
import com.example.ewallet.enums.Status;
import com.example.ewallet.exception.TransferFailedException;
import com.example.ewallet.exception.UncheckedException;
import com.example.ewallet.repository.*;
import com.example.ewallet.resourcesimpl.dto.*;
import com.example.ewallet.ServiceImpl.sms.SMSService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class UserService {

    private final SMSService smsService;
    private final UserSecurityCodeRepository userSecurityCodeRepository;
    private final UserAccRepository entityRepository;
    private final WalletRepository walletRepository;
    private final UserPicRepository userPicRepository;
    private final TransactionLogsRepository transactionLogsRepository;

    public UserService(SMSService smsService, UserSecurityCodeRepository userSecurityCodeRepository, UserAccRepository entityRepository, WalletRepository walletRepository, UserPicRepository userPicRepository, TransactionLogsRepository transactionLogsRepository) {
        this.smsService = smsService;
        this.userSecurityCodeRepository = userSecurityCodeRepository;
        this.entityRepository = entityRepository;
        this.walletRepository = walletRepository;
        this.userPicRepository = userPicRepository;
        this.transactionLogsRepository = transactionLogsRepository;
    }

    public Mono<ResponseEntity<ResOAuthDTO>> sendSecurityCodeBySMS(Long userId) {
        return smsService.smsSend(userId).map(securityCode -> {
            userSecurityCodeRepository.update(userId, securityCode, new Date(new Date().getTime() + 5000 * 60 * 1000));
            return ResponseEntity.ok(new ResOAuthDTO(userId, LoginResponse.OK));
        });
    }

    public Mono<ResponseEntity<ResOAuthDTO>> setPassword(Long phoneNumber, ReqSetPasswordDTO entity) {
        return Mono.just(userSecurityCodeRepository.findById(phoneNumber)).map(resultOptional -> {
            UserSecurityCode result = resultOptional.orElseThrow();
            if (!result.getSecurityCode().equals(entity.getSecurityCode()))
                return ResponseEntity.ok(new ResOAuthDTO(LoginResponse.SECURITY_CODE_IS_WRONG));
            if (result.getCodeExpirationDate().before(new Date()))
                return ResponseEntity.ok(new ResOAuthDTO(LoginResponse.SECURITY_CODE_HAS_BEEN_EXPIRED));

            entityRepository.updatePassword(phoneNumber, Hash.hash(entity.getPassword()));
            return ResponseEntity.ok(new ResOAuthDTO(phoneNumber, LoginResponse.OK));
        });
    }

    public Mono<ResponseEntity<ResGetAccountBalanceDTO>> getAccountBalance(Long phoneNumber) {

        return Mono.just(walletRepository.getAccountBalance(new UserAcc(phoneNumber))).map(res -> {
            if (res.isEmpty())
                throw new UncheckedException("there's no wallet for this User !");
            return ResponseEntity.ok(res.get());
        });
    }

    public Mono<ResponseEntity<Boolean>> updateProfile(Long userId, ReqUpdateProfileDTO entity) {

        entityRepository.updatePersonalInfo(userId, entity.getFirstName(), entity.getLastName(), entity.getEmail(), Status.ACTIVATE);

        if (entity.getPic() != null)
            return Mono.just(userPicRepository.findById(userId))
                    .flatMap(res -> {
                        if (res.isEmpty())
                            return Mono.just(userPicRepository.save(new UserPic(userId, entity.getPic()))).flatMap(saved -> Mono.just(ResponseEntity.ok(true)));
                        else
                            return Mono.just(userPicRepository.update(userId, entity.getPic())).flatMap(update -> Mono.just(ResponseEntity.ok(true)));
                    });
        else
            return Mono.just(ResponseEntity.ok(true));
    }

    public Mono<ResponseEntity<ResGetUserByPhoneNumberDTO>> getUserByPhoneNumber(Long id) {
        return Mono.just(entityRepository.getUserByPhoneNumber(id)).map(result -> {
            if (result.isEmpty())
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            else
                return ResponseEntity.ok(result.get());
        });
    }

    @Transactional(rollbackFor = TransferFailedException.class)
    public Mono<ResponseEntity<ResTransferDTO>> transferMoney(Long userId, ReqTransferDTO entity) throws TransferFailedException {

        UserAcc payerUser = new UserAcc(userId);
        UserAcc receiverUser = new UserAcc(entity.getDestAccountNumber());

        Double payerBalance = walletRepository.getBalanceByUser(payerUser, Status.DEACTIVATE);

        if (payerBalance == null)
            throw new UncheckedException("there's no activate wallet for this user");

        double balanceAfterPaying = payerBalance - entity.getAmount();
        if (balanceAfterPaying <= 0)
            throw new UncheckedException("Insufficient funds");

        Integer withdrawResult = walletRepository.withdraw(
                payerUser, balanceAfterPaying, Status.DEACTIVATE);
        if (withdrawResult != 1)
            throw new TransferFailedException("withdraw failed, some thing went wrong !");

        Integer depositResult = walletRepository.deposit(
                receiverUser, entity.getAmount(), Status.DEACTIVATE);
        if (depositResult != 1)
            throw new TransferFailedException("deposit failed, some thing went wrong !");

        Double balanceAfterReceiving = walletRepository.getBalanceByUser(
                receiverUser, Status.DEACTIVATE);

        Date occurrenceDate = new Date();

        //saving logs
        TransactionLogs payerLog = new TransactionLogs(
                entity.getAmount() * -1, occurrenceDate, balanceAfterPaying, payerUser, receiverUser);

        TransactionLogs receiverLog = new TransactionLogs(
                entity.getAmount(), occurrenceDate, balanceAfterReceiving, receiverUser, payerUser);

        TransactionLogs savedPayerLog = transactionLogsRepository.save(payerLog);
        TransactionLogs savedReceiverLog = transactionLogsRepository.save(receiverLog);

        Integer payerLogUpdateResult = transactionLogsRepository.updateCoupleRef(savedPayerLog.getId(), savedReceiverLog);
        Integer receiverLogUpdateResult = transactionLogsRepository.updateCoupleRef(savedReceiverLog.getId(), savedPayerLog);

        if (payerLogUpdateResult != 1 || receiverLogUpdateResult != 1)
            throw new TransferFailedException("log transaction error");

        return Mono.just(ResponseEntity.ok(new ResTransferDTO(userId, balanceAfterPaying)));
    }

    public Mono<ResponseEntity<Page<ResTurnoverDTO>>> getTurnoverByPagination(Long userId, Date fromDate, Date toDate, Pageable pageable) {

        if (toDate == null)
            toDate = new Date();

        if (fromDate == null)
            return Mono.just(transactionLogsRepository.reportToUserByPagination(userId, toDate, pageable)).map(ResponseEntity::ok);
        else
            return Mono.just(transactionLogsRepository.reportToUserByPagination(userId, fromDate, toDate, pageable)).map(ResponseEntity::ok);
    }
}
