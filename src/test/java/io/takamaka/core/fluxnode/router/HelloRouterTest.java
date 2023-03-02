/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package io.takamaka.core.fluxnode.router;

import static io.takamaka.core.fluxnode.TestUtils.HELLO_BASE_URI;
import io.takamaka.core.fluxnode.domain.HelloWorldBean;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

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
                    assert (responseBody != null);
                    assert (responseBody.getMessage() != null);
                    assert ("I'm a teapot".equals(responseBody.getMessage()));
                });
    }
    
    @Test
    public void testHelloWorldAdminUnauthorized() {
        webTestClient
                .get()
                .uri(HELLO_BASE_URI + "/makemeanadmincoffee")
                .exchange()
                //.expectStatus().isEqualTo(HttpStatus.I_AM_A_TEAPOT)
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);
//                .expectBody(HelloWorldBean.class)
//                .consumeWith((t) -> {
//                    HelloWorldBean responseBody = t.getResponseBody();
//                    assert (responseBody != null);
//                    assert (responseBody.getMessage() != null);
//                    assert ("I'm an admin teapot".equals(responseBody.getMessage()));
//                    
//                });
    }
    
    @Test
    public void testHelloWorldAdminAuthorized() {
        webTestClient
                .get()
                .uri(HELLO_BASE_URI + "/makemeanadmincoffee")
                .headers(headers -> headers.setBasicAuth("admin", "admin_password"))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.I_AM_A_TEAPOT)
                //.expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);
                .expectBody(HelloWorldBean.class)
                .consumeWith((t) -> {
                    HelloWorldBean responseBody = t.getResponseBody();
                    assert (responseBody != null);
                    assert (responseBody.getMessage() != null);
                    assert ("I'm an admin teapot".equals(responseBody.getMessage()));
                    
                });
    }
    
    @Test
    public void testHelloWorldUserUnauthorized() {
        webTestClient
                .get()
                .uri(HELLO_BASE_URI + "/makemeausercoffee")
                .exchange()
                //.expectStatus().isEqualTo(HttpStatus.I_AM_A_TEAPOT)
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);
//                .expectBody(HelloWorldBean.class)
//                .consumeWith((t) -> {
//                    HelloWorldBean responseBody = t.getResponseBody();
//                    assert (responseBody != null);
//                    assert (responseBody.getMessage() != null);
//                    assert ("I'm a user teapot".equals(responseBody.getMessage()));
//                });
    }
    
    @Test
    public void testHelloWorldUserAuthorized() {
        webTestClient
                .get()
                .uri(HELLO_BASE_URI + "/makemeausercoffee")
                .headers(headers -> headers.setBasicAuth("user", "user_password"))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.I_AM_A_TEAPOT)
                //.expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);
                .expectBody(HelloWorldBean.class)
                .consumeWith((t) -> {
                    HelloWorldBean responseBody = t.getResponseBody();
                    assert (responseBody != null);
                    assert (responseBody.getMessage() != null);
                    assert ("I'm a user teapot".equals(responseBody.getMessage()));
                });
    }
    
    @Test
    public void testHelloWorldUserToAdmin() {
        webTestClient
                .get()
                .uri(HELLO_BASE_URI + "/makemeanadmincoffee")
                .headers(headers -> headers.setBasicAuth("user", "user_password"))
                .exchange()
                //.expectStatus().isEqualTo(HttpStatus.I_AM_A_TEAPOT)
                .expectStatus().isEqualTo(HttpStatus.FORBIDDEN);
//                .expectBody(HelloWorldBean.class)
//                .consumeWith((t) -> {
//                    HelloWorldBean responseBody = t.getResponseBody();
//                    assert (responseBody != null);
//                    assert (responseBody.getMessage() != null);
//                    assert ("I'm a user teapot".equals(responseBody.getMessage()));
//                });
    }
    
    @Test
    public void testHelloWorldAdminToUser() {
        webTestClient
                .get()
                .uri(HELLO_BASE_URI + "/makemeausercoffee")
                .headers(headers -> headers.setBasicAuth("admin", "admin_password"))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.I_AM_A_TEAPOT)
                //.expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);
                .expectBody(HelloWorldBean.class)
                .consumeWith((t) -> {
                    HelloWorldBean responseBody = t.getResponseBody();
                    assert (responseBody != null);
                    assert (responseBody.getMessage() != null);
                    assert ("I'm a user teapot".equals(responseBody.getMessage()));
                });
    }
    
    @Test
    public void testHelloWorldAdminToNoauth() {
        webTestClient
                .get()
                .uri(HELLO_BASE_URI + "/makemeacoffee")
                .headers(headers -> headers.setBasicAuth("admin", "admin_password"))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.I_AM_A_TEAPOT)
                //.expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);
                .expectBody(HelloWorldBean.class)
                .consumeWith((t) -> {
                    HelloWorldBean responseBody = t.getResponseBody();
                    assert (responseBody != null);
                    assert (responseBody.getMessage() != null);
                    assert ("I'm a teapot".equals(responseBody.getMessage()));
                });
    }
    
    @Test
    public void testHelloWorldUsertoNoauth() {
        webTestClient
                .get()
                .uri(HELLO_BASE_URI + "/makemeacoffee")
                .headers(headers -> headers.setBasicAuth("user", "user_password"))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.I_AM_A_TEAPOT)
                //.expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);
                .expectBody(HelloWorldBean.class)
                .consumeWith((t) -> {
                    HelloWorldBean responseBody = t.getResponseBody();
                    assert (responseBody != null);
                    assert (responseBody.getMessage() != null);
                    assert ("I'm a teapot".equals(responseBody.getMessage()));
                });
    }

}
