package ru.otus.spring.exception;

public class CsvReadException extends Exception {
    public CsvReadException(String errorMessage) {
        super(errorMessage);
    }
}