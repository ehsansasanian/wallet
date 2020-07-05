package com.example.ewallet.security.configuration;

import com.example.ewallet.security.util.JwtTokenUtilConstants;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * This converter takes a SignedJWT and extracts all information contained to
 * build an Authentication Object The signed JWT has already been verified.
 *
 * @author
 */
public class UsernamePasswordAuthenticationBearer {

    public static Mono<Authentication> create(Claims claims) {
        String subject;
        String auths;
        List<GrantedAuthority> authorities = null;

        try {
            subject = claims.getSubject();

            /*
             * Get and check role list
             */
            List<String> roleList = claims.get(JwtTokenUtilConstants.CLAIM_KEY_ROLE_LIST, List.class);

            if (roleList == null || roleList.isEmpty())
                return Mono.empty();

            /*
             * Convert to GrantedAuthority model
             */
            authorities = new ArrayList(roleList.size());
            for (String role : roleList) {
                authorities.add(new SimpleGrantedAuthority(role));
            }

        } catch (Exception e) {
            return Mono.empty();
        }

        return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(subject, null, authorities));
    }
}
