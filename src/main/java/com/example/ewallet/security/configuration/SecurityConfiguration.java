package com.example.ewallet.security.configuration;

import com.example.ewallet.security.component.AuthenticationManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * @author EhSan
 */
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
@Log4j2
public class SecurityConfiguration {

    @Value("${jwt.token.header}")
    private String tokenHeaderName;

    @Value("${jwt.token.param}")
    private String tokenParamName;

    @Value("${jwt.token.public-key}")
    private String jwtTokenPublicKey;

    @Value("${jwt.token.prefix}")
    private String jwtTokenPrefix;

    @Value("${jwt.token.expiration}")
    private Long jwtTokenExpiration;

    /**
     * For Spring Security webflux, a chain of filters will provide user
     * authentication and authorization, we add custom filters to enable JWT token
     * approach.
     *
     * @param http An initial object to build common filter scenarios. Customized
     *             filters are added here.
     * @return SecurityWebFilterChain A filter chain for web exchanges that will
     * provide security
     **/
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        log.debug("Initializing the security configuration");
        http.csrf().disable().authorizeExchange().and().authorizeExchange()

                /* Signup and Login */
                .pathMatchers(HttpMethod.POST, "/oauth/**").permitAll() //
                .pathMatchers(HttpMethod.GET, "/oauth/**").permitAll() //

                .pathMatchers("/**").authenticated().and()
                /*
                 * Custom filters to enable JWT token authentication and authorization.
                 */
                .addFilterAt(bearerAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION);
        return http.build();
    }

    /**
     * Use the already implemented logic by AuthenticationWebFilter and set a custom
     * converter that will handle requests containing a Bearer token inside the HTTP
     * Authorization header. Set a dummy authentication manager to this filter, it's
     * not needed because the converter handles this.
     *
     * @return bearerAuthenticationFilter that will authorize requests containing a
     * JWT
     */
    private AuthenticationWebFilter bearerAuthenticationFilter() {
        /*
         * Object for parse and check bearer token
         */
        Function<ServerWebExchange, Mono<Authentication>> bearerConverter = new
                ServerHttpBearerAuthenticationConverter(
                tokenHeaderName, tokenParamName, jwtTokenPrefix, jwtTokenPublicKey, jwtTokenExpiration);

        ReactiveAuthenticationManager authManager = new AuthenticationManager();
        AuthenticationWebFilter bearerAuthenticationFilter = new AuthenticationWebFilter(authManager);

        bearerAuthenticationFilter.setAuthenticationConverter(bearerConverter);

        /*
         * Set requires authentication uris, All uris (/**) are authenticated.
         */
        bearerAuthenticationFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/**"));

        return bearerAuthenticationFilter;
    }

}
