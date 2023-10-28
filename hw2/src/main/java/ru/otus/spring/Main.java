package ru.otus.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.spring.exception.CsvReadException;
import ru.otus.spring.service.TestRunnerService;

@ComponentScan
public class Main {
    public static void main(String[] args) throws CsvReadException {
        new AnnotationConfigApplicationContext(Main.class).getBean(TestRunnerService.class).run();
    }
}
