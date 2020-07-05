package com.example.ewallet.ServiceImpl;

import com.example.ewallet.ServiceImpl.sms.SMSService;
import com.example.ewallet.base.GenerateRandomChars;
import com.example.ewallet.base.Print;
import com.example.ewallet.domain.TempUser;
import com.example.ewallet.domain.UserAcc;
import com.example.ewallet.domain.UserSecurityCode;
import com.example.ewallet.domain.Wallet;
import com.example.ewallet.enums.LoginResponse;
import com.example.ewallet.enums.Status;
import com.example.ewallet.enums.UserRole;
import com.example.ewallet.exception.UncheckedException;
import com.example.ewallet.repository.TempUserRepository;
import com.example.ewallet.repository.UserRepository;
import com.example.ewallet.repository.UserSecurityCodeRepository;
import com.example.ewallet.repository.WalletRepository;
import com.example.ewallet.resourcesimpl.dto.ReqOAuthDTO;
import com.example.ewallet.resourcesimpl.dto.ResOAuthDTO;
import com.example.ewallet.resourcesimpl.dto.ResTokenDTO;
import com.example.ewallet.security.util.SecurityUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

/**
 * @author EhSan
 */
@Service
@RequiredArgsConstructor
public class OAuthService {

    private final TempUserRepository tempUserRepository;
    private final UserRepository userRepository;
    private final SMSService smsService;
    private final UserSecurityCodeRepository userSecurityCodeRepository;
    private final SecurityUtil securityUtil;
    private final WalletRepository walletRepository;

    private final int smsCodeLength = 5;


    public Mono<ResponseEntity<ResOAuthDTO>> userRegistry(Long num) {

        //check if user exists in db
        return Mono.just(userRepository.findById(num)).flatMap(ua -> {
            if (ua.isPresent())
                return Mono.just(ResponseEntity.ok(new ResOAuthDTO(LoginResponse.USER_ALREADY_EXISTS)));


            return Mono.just(tempUserRepository.findById(num)).flatMap(tu -> {
                if (tu.isPresent() && tu.get().getCodeExpiration() != null && tu.get().getCodeExpiration().after(new Date()))
                    return Mono.just(ResponseEntity.ok(new ResOAuthDTO(LoginResponse.WAIT_FOR_SMS)));

                //generate (securityCode)
                Mono<String> securityCode = smsService.smsSend(num);
                Date date = new Date(new Date().getTime() + 5 * 60 * 1000);

                //save num,date,secCode in TempUser table
                if (tu.isPresent() && tu.get().getCodeExpiration() != null && tu.get().getCodeExpiration().before(new Date()))
                    tempUserRepository.update(num, securityCode.block(), date);
                else {
                    tempUserRepository.save(new TempUser(num, securityCode.block(), new Date()));
                }

                return Mono.just(ResponseEntity.ok(new ResOAuthDTO(num, LoginResponse.OK)));
            });
        });

    }

    @Transactional
    public Mono<ResponseEntity<ResOAuthDTO>> signupSecurityCodeValidator(ReqOAuthDTO entity) {

        //fetch User by ID from DB
        return Mono.just(tempUserRepository.findById(entity.getUserId())).flatMap(optionalTempUser -> {

            //check if user doesn't exist ?
            if (optionalTempUser.isEmpty())
                return Mono.just(ResponseEntity.ok(new ResOAuthDTO(LoginResponse.USER_NOT_FOUND)));

            TempUser tempUser = optionalTempUser.get();

            //check if securityCode is expired
            if (tempUser.getCodeExpiration().after(new Date()))
                return Mono.just(ResponseEntity.ok(new ResOAuthDTO(LoginResponse.SECURITY_CODE_HAS_BEEN_EXPIRED)));

            //check if securityCode is wrong
            if (!tempUser.getSecurityCode().equals(entity.getSecurityCode()))
                return Mono.just(ResponseEntity.ok(new ResOAuthDTO(LoginResponse.SECURITY_CODE_IS_WRONG)));

            //save User into UserAccount
            return Mono.just(userRepository.save(new UserAcc(entity.getUserId(), Status.INCOMPLETE_INFORMATION, UserRole.ROLE_USER))).flatMap(savedUserAcc -> {

                //save record for securityCode
                userSecurityCodeRepository.save(new UserSecurityCode(savedUserAcc.getPhoneNumber()));

                //create wallet
                walletRepository.save(new Wallet(0D, new Date(), Status.ACTIVATE, savedUserAcc));

                //clear User from TempUser
                tempUserRepository.deleteById(entity.getUserId());

                return Mono.just(ResponseEntity.ok(new ResOAuthDTO(savedUserAcc.getPhoneNumber(), LoginResponse.OK)));
            });

        });

    }

    public Mono<ResponseEntity<ResOAuthDTO>> requestSecurityCodeForLogin(Long phoneNum) {

        return Mono.just(userRepository.findById(phoneNum)).flatMap(user -> {

            if (user.isEmpty())
                return Mono.just(ResponseEntity.ok(new ResOAuthDTO(LoginResponse.ACCOUNT_NOT_FOUND)));

            Status status = user.get().getStatus();

            if (status == Status.DEACTIVATE)
                return Mono.just(ResponseEntity.ok(new ResOAuthDTO(LoginResponse.ACCOUNT_IS_LOCKED)));

            //generate (securityCode)
            String securityCode = GenerateRandomChars.generatedCode(smsCodeLength);

            //securityCode Expiration
            Date date = new Date(new Date().getTime() + 5000 * 60 * 1000);
            userSecurityCodeRepository.update(phoneNum, securityCode, date);

            return Mono.just(ResponseEntity.ok(new ResOAuthDTO(LoginResponse.OK)));
        });
    }

    public Mono<ResponseEntity<ResTokenDTO>> loginBySecurityCodeAndGetToken(ReqOAuthDTO entity) {

        return Mono.just(userSecurityCodeRepository.findById(entity.getUserId())).flatMap(a -> {
            if (a.isEmpty())
                return Mono.error(new UncheckedException("User Not Fount"));

            if (a.get().getCodeExpirationDate().before(new Date()))
                return Mono.just(ResponseEntity.ok(new ResTokenDTO(ResTokenDTO.Status.EXPIRED_CODE_DATE)));

            if (!a.get().getSecurityCode().equals(entity.getSecurityCode()))
                return Mono.just(ResponseEntity.ok(new ResTokenDTO(ResTokenDTO.Status.INVALID_CODE)));

            return Mono.just(userRepository.findById(entity.getUserId())).flatMap(user -> {
                Print.print(user.get());

                if (user.isEmpty())
                    return Mono.error(new UncheckedException("User Not Fount"));

                return Mono.just(securityUtil.generateTokenByUserDetail(user.get().getPhoneNumber(), Collections.singletonList(user.get().getUserRole().toString())))
                        .flatMap(token -> Mono.just(securityUtil.generateRefreshToken(user.get().getPhoneNumber(), user.get().getUserRole().toString()))
                                .flatMap(refreshToken -> Mono.just(ResponseEntity.ok(new ResTokenDTO(token, refreshToken, ResTokenDTO.Status.OK)))));
            });
        });
    }

    public Mono<ResponseEntity<ResTokenDTO>> renewToken(String refreshToken, String token) {

        /*
         * Regenerate token with new date
         */

        Claims claimsToken = securityUtil.getJwtTokenUtil().getClaimsFromToken(token);

        Map<String, Object> claimsTokenMap = securityUtil.renewToken(claimsToken);

        /*
         * Regenerate refresh token with new date
         */
        Claims claimsRefreshToken = securityUtil.getJwtTokenUtil().getClaimsFromRefreshToken(refreshToken.substring(7));

        System.out.println(claimsRefreshToken + "\nclaimsRefreshToken");
        Map<String, Object> claimsRefreshTokenMap = securityUtil.renewToken(claimsRefreshToken);

        return Mono.just(ResponseEntity.ok().body(
                new ResTokenDTO(
                        securityUtil.getJwtTokenUtil().generateToken(claimsTokenMap),
                        securityUtil.getJwtTokenUtil().generateRefreshToken(claimsRefreshTokenMap),
                        ResTokenDTO.Status.OK)));


    }


}
