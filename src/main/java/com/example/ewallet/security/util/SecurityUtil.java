package com.example.ewallet.security.util;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author EhSan
 */
@Component
public class SecurityUtil {


    @Value("${jwt.token.private-key}")
    private String jwtTokenPrivateKey;

    @Value("${jwt.token.expiration}")
    private Long jwtTokenExpiration;

    @Value("${jwt.refresh-token.private-key}")
    private String jwtRefreshTokenPrivateKey;

    @Value("${jwt.refresh-token.expiration}")
    private Long jwtRefreshTokenExpiration;

    public JwtTokenUtil getJwtTokenUtil() {
        return new JwtTokenUtil(jwtTokenPrivateKey, jwtTokenExpiration, jwtRefreshTokenPrivateKey, jwtRefreshTokenExpiration);
    }



    public String generateTokenByUserDetail(Long userId, List<String> role) {

        Map<String, Object> claims = new HashMap<>();

        claims.put(JwtTokenUtilConstants.CLAIM_KEY_USER_ID, userId);

        claims.put(JwtTokenUtilConstants.CLAIM_KEY_ROLE_LIST, role);


        System.out.println(claims + " claims ---------");

        return getJwtTokenUtil().generateTokenByClaim(claims);

    }

    public String generateRefreshToken(Long userId, String roleSet) {

        Map<String, Object> claims = new HashMap<>();

        claims.put(JwtTokenUtilConstants.CLAIM_KEY_USER_ID, userId);


        claims.put(JwtTokenUtilConstants.CLAIM_KEY_ROLE_LIST, roleSet);
        claims.put(JwtTokenUtilConstants.CLAIM_KEY_TOKEN_TYPE, JwtTokenUtilConstants.TOKEN_TYPE_REFRESH_TOKEN);
        return getJwtTokenUtil().generateRefreshTokenByClient(claims);

    }

    public Map<String, Object> renewToken(Claims claimsToken) {
        Map<String, Object> claimsTokenMap = new HashMap<>();

        System.out.println(claimsToken + "before forEach -------");

        claimsToken.forEach((key, value) -> {
            if (key.equals(JwtTokenUtilConstants.CLAIM_KEY_CREATED)) {
                claimsTokenMap.put(key, new Date());
            } else {
                claimsTokenMap.put(key, value);
            }
        });
        System.out.println(claimsTokenMap + "after forEach -------");
        return claimsTokenMap;
    }
}
