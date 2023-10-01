package ru.otus.spring.dao;

import ru.otus.spring.exception.CsvReadException;

import java.util.List;

public interface SurveyDao {
    List<List<String>> parseCsvToSurvey() throws CsvReadException;
}