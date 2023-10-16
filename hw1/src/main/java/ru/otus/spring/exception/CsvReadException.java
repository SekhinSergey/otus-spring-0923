package ru.otus.spring.exception;

public class CsvReadException extends RuntimeException {
    public CsvReadException(String errorMessage) {
        super(errorMessage);
    }
}
