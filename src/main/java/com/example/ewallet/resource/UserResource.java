package com.example.ewallet.resource;

import com.example.ewallet.constants.RoleList;
import com.example.ewallet.exception.TransferFailedException;
import com.example.ewallet.resourcesimpl.dto.*;
import com.example.ewallet.security.util.JwtTokenUtilConstants;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Date;

@RequestMapping("/user")
public interface UserResource {

    @GetMapping("/get-security-code")
    Mono<ResponseEntity<ResOAuthDTO>> sendSecurityCodeBySMS(@RequestHeader(JwtTokenUtilConstants.CLAIM_KEY_USER_ID) String userId);

    @PostMapping("/set-password")
    @PreAuthorize(RoleList.PA_USER)
    Mono<ResponseEntity<ResOAuthDTO>> setPassword(@RequestHeader(JwtTokenUtilConstants.CLAIM_KEY_USER_ID) String userId, @RequestBody ReqSetPasswordDTO entity);

    @GetMapping("/balance")
    @PreAuthorize(RoleList.PA_USER)
    Mono<ResponseEntity<ResGetAccountBalanceDTO>> getAccountBalance(@RequestHeader("userId") Long phoneNumber);

    @PostMapping("/update-profile")
    @PreAuthorize(RoleList.PA_USER)
    Mono<ResponseEntity<Boolean>> updateProfile(@RequestHeader(JwtTokenUtilConstants.CLAIM_KEY_USER_ID) String userId, @RequestBody ReqUpdateProfileDTO entity);

    @GetMapping("/get/{phoneNum}")
    @PreAuthorize(RoleList.PA_USER)
    Mono<ResponseEntity<ResGetUserByPhoneNumberDTO>> getUserByPhoneNumber(@PathVariable("phoneNum") Long phoneNum);

    @PostMapping("/transfer")
    @PreAuthorize(RoleList.PA_USER)
    Mono<ResponseEntity<ResTransferDTO>> transfer(@RequestHeader("userId") Long userId, @RequestBody ReqTransferDTO entity) throws TransferFailedException;

    @GetMapping("/wallet-history")
    @PreAuthorize(RoleList.PA_USER)
    Mono<ResponseEntity<Page<ResTurnoverDTO>>> turnoverByPagination(@RequestHeader(JwtTokenUtilConstants.CLAIM_KEY_USER_ID) String userId,
                                                                    @RequestParam(value = "fromDate", required = false) Date fromDate,
                                                                    @RequestParam(value = "toDate", required = false) Date toDate,
                                                                    @RequestParam("page") Integer page, @RequestParam("size") Integer size);

}
