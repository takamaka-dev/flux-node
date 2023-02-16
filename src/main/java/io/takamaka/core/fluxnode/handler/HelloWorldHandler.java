/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.takamaka.core.fluxnode.handler;

import io.takamaka.core.fluxnode.domain.HelloWorldBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 *
 * @author Giovanni Antino giovanni.antino@takamaka.io
 */
@Component
@Slf4j
public class HelloWorldHandler {

    public Mono<ServerResponse> makeMeACoffee(ServerRequest request) {
        return ServerResponse.status(HttpStatus.I_AM_A_TEAPOT).bodyValue(new HelloWorldBean("I'm a teapot"));
    }
}
