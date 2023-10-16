package ru.otus.spring.service;

import ru.otus.spring.exception.CsvReadException;

public interface QuestionService {
    void printQuestion() throws CsvReadException;
}
