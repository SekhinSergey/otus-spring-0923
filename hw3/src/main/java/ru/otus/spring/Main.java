package ru.otus.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.otus.spring.exception.CsvReadException;
import ru.otus.spring.service.TestRunnerService;

@SpringBootApplication
public class Main {
    public static void main(String[] args) throws CsvReadException {
        // Please, select profile before running
        ApplicationContext context = SpringApplication.run(Main.class, args);
        context.getBean(TestRunnerService.class).run();
    }
}
