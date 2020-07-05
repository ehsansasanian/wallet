package com.example.ewallet.resourcesimpl;

import com.example.ewallet.ServiceImpl.OAuthService;
import com.example.ewallet.base.ValidationTools;
import com.example.ewallet.resourcesimpl.dto.ReqOAuthDTO;
import com.example.ewallet.resourcesimpl.dto.ResOAuthDTO;
import com.example.ewallet.resourcesimpl.dto.ResTokenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * @author EhSan
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OAuthResource {

    private final OAuthService entityService;

    @GetMapping("/signup/{phoneNum}")
    public Mono<ResponseEntity<ResOAuthDTO>> signup(@PathVariable("phoneNum") Long phoneNum) {
        ValidationTools.phoneNumber(phoneNum, "phoneNum");
        return entityService.userRegistry(phoneNum);
    }

    @PostMapping("/signup-validation")
    public Mono<ResponseEntity<ResOAuthDTO>> signupValidation(@RequestBody ReqOAuthDTO entity) {
        entity.validation();
        return entityService.signupSecurityCodeValidator(entity);
    }

    //get SMS
    @GetMapping("/login/{phoneNum}")
    public Mono<ResponseEntity<ResOAuthDTO>> requestSecurityCodeForLogin(@PathVariable("phoneNum") Long phoneNum) {
        ValidationTools.phoneNumber(phoneNum, "phoneNum");
        return entityService.requestSecurityCodeForLogin(phoneNum);
    }

    //sms validation for token
    @PostMapping("/token")
    public Mono<ResponseEntity<ResTokenDTO>> loginBySecurityCodeAndGetToken(@RequestBody ReqOAuthDTO entity) {
        entity.validation();
        return entityService.loginBySecurityCodeAndGetToken(entity);
    }

    @GetMapping("token-renew")
    public Mono<ResponseEntity<ResTokenDTO>> renewToken(@RequestHeader("Authorization") String refreshToken, @RequestParam("token") String token) {
        return entityService.renewToken(refreshToken, token);
    }
}
