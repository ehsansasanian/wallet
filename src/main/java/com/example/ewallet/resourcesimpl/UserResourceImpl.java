package com.example.ewallet.resourcesimpl;

import com.example.ewallet.base.Print;
import com.example.ewallet.base.ValidationTools;
import com.example.ewallet.exception.TransferFailedException;
import com.example.ewallet.resource.UserResource;
import com.example.ewallet.resourcesimpl.dto.*;
import com.example.ewallet.ServiceImpl.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Date;

@RestController
public class UserResourceImpl
        implements UserResource {

    private final UserService entityService;

    @Value("${sms.code.length}")
    private String smsCodeLength;

    public UserResourceImpl(UserService entityService) {
        this.entityService = entityService;
    }

    @Override
    public Mono<ResponseEntity<ResOAuthDTO>> sendSecurityCodeBySMS(String userId) {
        ValidationTools.phoneNumber(userId, "user phone number");
        return entityService.sendSecurityCodeBySMS(Long.valueOf(userId));
    }

    @Override
    public Mono<ResponseEntity<ResOAuthDTO>> setPassword(String userId, ReqSetPasswordDTO entity) {
        entity.validation(userId, smsCodeLength);
        return entityService.setPassword(Long.valueOf(userId), entity);
    }

    @Override
    public Mono<ResponseEntity<ResGetAccountBalanceDTO>> getAccountBalance(Long phoneNumber) {
        ValidationTools.phoneNumber(phoneNumber, "phoneNumber");
        return entityService.getAccountBalance(phoneNumber);
    }

    @Override
    public Mono<ResponseEntity<Boolean>> updateProfile(String userId, ReqUpdateProfileDTO entity) {
        entity.validation(userId);
        return entityService.updateProfile(Long.valueOf(userId), entity);
    }

    @Override
    public Mono<ResponseEntity<ResGetUserByPhoneNumberDTO>> getUserByPhoneNumber(Long id) {
        ValidationTools.phoneNumber(id, "phoneNumber");
        return entityService.getUserByPhoneNumber(id);
    }

    @Override
    public Mono<ResponseEntity<ResTransferDTO>> transfer(Long userId, ReqTransferDTO entity) throws TransferFailedException {
        System.out.println(userId);
        Print.print(entity);
        entity.validation(userId);
        return entityService.transferMoney(userId, entity);
    }

    @Override
    public Mono<ResponseEntity<Page<ResTurnoverDTO>>> turnoverByPagination(String userId, Date fromDate, Date toDate, Integer page, Integer size) {
        ValidationTools.phoneNumber(userId, "userId");
        ValidationTools.nullObject(page, "page");
        ValidationTools.nullObject(size, "size");
        return entityService.getTurnoverByPagination(Long.valueOf(userId), fromDate, toDate, PageRequest.of(page, size));
    }
}
