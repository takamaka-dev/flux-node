/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.takamaka.core.fluxnode.router;

import io.takamaka.core.fluxnode.handler.HelloWorldHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import org.springframework.web.reactive.function.server.RouterFunction;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * Server test route
 *
 * @author Giovanni Antino giovanni.antino@takamaka.io
 */
@Configuration
public class HelloRouter {

    @Bean
    RouterFunction<ServerResponse> helloRoute(HelloWorldHandler helloWorldHandler) {
        return route()
                .nest(path("/helloworld"),
                        (builder) -> {
                            builder
                                    .GET("/makemeacoffee", (request) -> helloWorldHandler.makeMeACoffee(request))
                                    .GET("/makemeanadmincoffee", (request) -> helloWorldHandler.makeMeAnAdminCoffee(request))
                                    .GET("/makemeausercoffee", (request) -> helloWorldHandler.makeMeAUserCoffee(request));
                        }).build();
    }

}
