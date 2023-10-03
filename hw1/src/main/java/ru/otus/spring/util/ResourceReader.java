package ru.otus.spring.util;

import ru.otus.spring.exception.CsvReadException;

import java.util.List;

public interface ResourceReader {

    List<List<String>> readResource(String fileName) throws CsvReadException;
}
