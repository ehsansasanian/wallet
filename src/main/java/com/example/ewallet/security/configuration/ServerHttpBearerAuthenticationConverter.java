package com.example.ewallet.security.configuration;

import com.example.ewallet.security.util.AuthorizationHeaderPayload;
import com.example.ewallet.security.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * This converter extracts a bearer token from a WebExchange and returns an
 * Authentication object if the JWT token is valid. Validity means is well
 * formed and signature is correct
 *
 * @author EhSan
 */
public class ServerHttpBearerAuthenticationConverter implements Function<ServerWebExchange, Mono<Authentication>> {

    private final String tokenHeaderName;
    private final String tokenParamName;
    private final String jwtTokenPublicKey;
    private final Long jwtTokenExpiration;
    private String jwtTokenPrefix;

    public ServerHttpBearerAuthenticationConverter(String tokenHeaderName, String tokenParamName, String jwtTokenPrefix,
                                                   String jwtTokenPublicKey, Long jwtTokenExpiration) {
        super();
        this.tokenHeaderName = tokenHeaderName;
        this.tokenParamName = tokenParamName;
        this.jwtTokenPrefix = jwtTokenPrefix;
        this.jwtTokenPublicKey = jwtTokenPublicKey;
        this.jwtTokenExpiration = jwtTokenExpiration;
    }

    private final Predicate<String> matchBearerLength = authValue -> authValue.length() > jwtTokenPrefix.length();
    private final Function<String, Mono<String>> isolateBearerValue = authValue -> Mono
            .justOrEmpty(authValue.substring(jwtTokenPrefix.length()));

    private final JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

    /**
     * Apply this function to the current WebExchange, an Authentication object is
     * returned when completed.
     *
     * @param serverWebExchange
     * @return
     */
    @Override
    public Mono<Authentication> apply(ServerWebExchange serverWebExchange) {
        return Mono.justOrEmpty(serverWebExchange) //
                /*
                 * Extract token from serverWebExchange
                 */
                .flatMap(exchange -> AuthorizationHeaderPayload.extract(exchange, tokenHeaderName, tokenParamName))
                .filter(this.matchBearerLength) // Check token length is OK
                .flatMap(this.isolateBearerValue) // Remove 'Bearer ' from begin of the token
                .flatMap(token -> Mono.just(jwtTokenUtil.check(token, jwtTokenPublicKey, jwtTokenExpiration))) //
                .flatMap(claims -> this.setClaimsToHeader(claims, serverWebExchange)) // Set token info into header
                .flatMap(UsernamePasswordAuthenticationBearer::create) //
                .log();
    }

    /**
     * Read claims data and set into request header
     *
     * @param monoClaims
     * @param serverWebExchange
     * @return
     */
    private Mono<Claims> setClaimsToHeader(Mono<Claims> monoClaims, ServerWebExchange serverWebExchange) {

        return monoClaims.flatMap(claims -> {
            /*
             * Set token information to header
             */
            serverWebExchange.getRequest().mutate().headers(header -> {

                for (Entry<String, Object> each : claims.entrySet()) {
                    /*
                     * If object is String
                     */
                    if (each.getValue() instanceof Long)
                        header.add(each.getKey(), each.getValue().toString());

                        /*
                         * If object is List
                         */
                    else if (each.getValue() instanceof List<?>)
                        header.addAll(each.getKey(), (List) each.getValue());

                }
            });

            return monoClaims;
        });
    }
}
