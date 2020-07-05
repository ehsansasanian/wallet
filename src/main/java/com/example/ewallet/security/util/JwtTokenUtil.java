package com.example.ewallet.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Decides when a JWT string is valid. First try to parse it, then check that
 * the signature is correct. If something fails an empty Mono is returning
 * meaning that is not valid. Verify that expiration date is valid
 *
 * @author EhSan
 */
public class JwtTokenUtil implements Serializable {

    private String tokenSecret;

    private Long tokenExpiration;

    private String refreshTokenSecret;

    private Long refreshTokenExpiration;

    public JwtTokenUtil(String jwtTokenPrivateKey, Long jwtTokenExpiration, String jwtRefreshTokenPrivateKey, Long jwtRefreshTokenExpiration) {
        super();
        this.tokenSecret = jwtTokenPrivateKey;
        this.tokenExpiration = jwtTokenExpiration;
        this.refreshTokenSecret = jwtRefreshTokenPrivateKey;
        this.refreshTokenExpiration = jwtRefreshTokenExpiration;
    }

    public String generateToken(Map<String, Object> claims) {
        return Jwts.builder().setClaims(claims).setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, tokenSecret).compact();
    }

    public String generateRefreshToken(Map<String, Object> claims) {
        return Jwts.builder().setClaims(claims).setExpiration(generateRefreshTokenExpirationDate())
                .signWith(SignatureAlgorithm.HS512, refreshTokenSecret).compact();
    }

    public JwtTokenUtil() {

    }

    public String generateTokenByClaim(Map<String, Object> claims) {
        return Jwts.builder().setClaims(claims).setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, tokenSecret).compact();
    }

    public String generateRefreshTokenByClient(Map<String, Object> claims) {
        return Jwts.builder().setClaims(claims).setExpiration(generateRefreshTokenExpirationDate())
                .signWith(SignatureAlgorithm.HS512, refreshTokenSecret).compact();
    }


    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + tokenExpiration * 1000);
    }

    private Date generateRefreshTokenExpirationDate() {
        return new Date(System.currentTimeMillis() + refreshTokenExpiration * 1000);
    }

    public Mono<Claims> check(String token, String jwtTokenPublicKey, Long jwtTokenExpiration) {

        return Mono.justOrEmpty(createClaimsAndVerify(token, jwtTokenPublicKey)) //
                .filter(claims -> isNotExpired(claims, jwtTokenExpiration));
    }

    private Claims createClaimsAndVerify(String token, String key) {
        try {
            return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Boolean isNotExpired(Claims claims, Long tokenExpiration) {
        Date expiration = new Date(claims.getExpiration().getTime() + tokenExpiration);
        return !expiration.before(new Date());
    }

    public Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    public Claims getClaimsFromRefreshToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(refreshTokenSecret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }
}