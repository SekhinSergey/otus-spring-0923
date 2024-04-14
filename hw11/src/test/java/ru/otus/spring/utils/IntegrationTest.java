package ru.otus.spring.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(classes = WebTestClientConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    @Autowired
    public WebTestClient webTestClientBuild;
}
