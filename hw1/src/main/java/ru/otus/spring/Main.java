package ru.otus.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.spring.exception.CsvReadException;
import ru.otus.spring.service.SurveyService;

public class Main {
    public static void main(String[] args) throws CsvReadException {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-context.xml");
        SurveyService surveyService = context.getBean(SurveyService.class);
        surveyService.getSurveys().forEach(System.out::println);
    }
}