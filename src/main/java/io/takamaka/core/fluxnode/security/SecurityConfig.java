/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.takamaka.core.fluxnode.security;

import io.takamaka.wallet.utils.FileHelper;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;

/**
 *
 * @author Giovanni Antino giovanni.antino@takamaka.io
 */
@Configuration
@EnableWebFluxSecurity
@Slf4j
public class SecurityConfig {

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Bean
    SecurityWebFilterChain adminHttpSecurity(ServerHttpSecurity http) {
        http.
                securityMatcher(
                        new PathPatternParserServerWebExchangeMatcher("/helloworld/makemeanadmincoffee")
                )
                .authorizeExchange(
                        (exchanges) -> exchanges.anyExchange().hasRole("ADMIN")//.authenticated()
                )
                .httpBasic();
        return http.build();
    }

    @Bean
    SecurityWebFilterChain userHttpSecurity(ServerHttpSecurity http) {
        http.
                securityMatcher(
                        new PathPatternParserServerWebExchangeMatcher("/helloworld/makemeausercoffee")
                )
                .authorizeExchange(
                        (exchanges) -> exchanges.anyExchange().hasRole("USER")//.authenticated()
                )
                .httpBasic();
        return http.build();
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        log.info("Loading env");

        //if(FileHelper.fileExists())
        ConcurrentSkipListMap<String, UserDetails> users = new ConcurrentSkipListMap<>();
        //        log.info(encoder.encode("admin_password"));
        UserDetails admin = User.withUsername("admin")
                .password("{bcrypt}$2a$10$csgn3rKfs5nbxnp/fEC93..0ZBk8ud7NdHFeziW/N4dhc.vlsDj7y")
                .roles("ADMIN", "USER")
                .build();
        //        log.info(encoder.encode("user_password"));
        UserDetails user = User.withUsername("user")
                .password("{bcrypt}$2a$10$n8r/ZKeL984X7cnI9ottVePGNKKZcWmwbvEVydOZcF4WerQJrf7zW")
                .roles("USER")
                .build();
        users.put("admin", admin);
        users.put("user", user);
        return new MapReactiveUserDetailsService(users.values());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        log.info(encoder.encode("admin_password"));

        return encoder;
    }

}
