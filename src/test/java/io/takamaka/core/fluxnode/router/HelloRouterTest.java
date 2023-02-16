/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package io.takamaka.core.fluxnode.router;

import static io.takamaka.core.fluxnode.TestUtils.HELLO_BASE_URI;
import io.takamaka.core.fluxnode.domain.HelloWorldBean;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author Giovanni Antino giovanni.antino@takamaka.io
 */
@SpringBootTest
@TestPropertySource("classpath:server-config-test.properties")
@AutoConfigureWebTestClient
@Slf4j
public class HelloRouterTest {

    @Autowired
    WebTestClient webTestClient;

    public HelloRouterTest() {
    }

    @Test
    public void testHelloWorld() {
        webTestClient
                .get()
                .uri(HELLO_BASE_URI + "/makemeacoffee")
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.I_AM_A_TEAPOT)
                .expectBody(HelloWorldBean.class)
                .consumeWith((t) -> {
                    HelloWorldBean responseBody = t.getResponseBody();
                    assert (responseBody.getMessage().equals("I'm a teapot"));
                });
    }

}
