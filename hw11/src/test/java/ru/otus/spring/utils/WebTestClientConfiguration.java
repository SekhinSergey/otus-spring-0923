package ru.otus.spring.utils;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.web.reactive.server.WebTestClient;

@Lazy
@TestConfiguration
public class WebTestClientConfiguration {

    @LocalServerPort
    private int port;

    @Bean
    public WebTestClient webTestClientBuild() {
        return WebTestClient
                .bindToServer()
                .baseUrl(String.format("http://localhost:%d", port))
                .build();
    }
}
