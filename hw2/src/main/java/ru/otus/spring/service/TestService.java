package ru.otus.spring.service;

import ru.otus.spring.exception.CsvReadException;

public interface TestService {

    void printTest() throws CsvReadException;
}
