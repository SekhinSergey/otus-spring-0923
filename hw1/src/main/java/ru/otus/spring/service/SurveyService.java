package ru.otus.spring.service;

import ru.otus.spring.domain.Survey;
import ru.otus.spring.exception.CsvReadException;

import java.util.List;

public interface SurveyService {
    List<Survey> getSurveys() throws CsvReadException;
}