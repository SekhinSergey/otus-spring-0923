package ru.otus.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.spring.exception.CsvReadException;
import ru.otus.spring.service.QuestionService;

public class Main {
    public static void main(String[] args) throws CsvReadException {
        new ClassPathXmlApplicationContext("spring-context.xml").getBean(QuestionService.class).printQuestion();
        // Запускать hw1-jar-with-dependencies.jar
        // Замечание при запуске джарника. Решение на данный момент не найдено
    }
}
