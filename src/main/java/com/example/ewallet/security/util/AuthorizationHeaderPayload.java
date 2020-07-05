package com.example.ewallet.security.util;

import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * @author EhSan
 */
public class AuthorizationHeaderPayload {

    /*
     * Extract token from request
     */
    public static Mono<String> extract(ServerWebExchange serverWebExchange, String tokenHeaderName,
                                       String tokenParamName) {
        /*
         * Extract token from header
         */
        return Mono.justOrEmpty(serverWebExchange.getRequest().getHeaders().getFirst(tokenHeaderName));
        /*
         * If can not found token in header try to get token from query param
         */
//				.switchIfEmpty(Mono.just(serverWebExchange.getRequest().getQueryParams().getFirst(tokenParamName)))
//				.switchIfEmpty(Mono.just(""));
    }
}
