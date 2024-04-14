package ru.otus.spring;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableMongock
@SpringBootApplication
@EnableReactiveMongoRepositories
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
