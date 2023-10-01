package ru.otus.spring.dao;

import ru.otus.spring.exception.CsvReadException;
import ru.otus.spring.util.ResourceReader;

import java.util.List;

public class SurveyDaoImpl implements SurveyDao {

    private final ResourceReader resourceReader;

    private final String resourceName;

    public SurveyDaoImpl(ResourceReader resourceReader, String resourceName) {
        this.resourceReader = resourceReader;
        this.resourceName = resourceName;
    }

    public List<List<String>> parseCsvToSurvey() throws CsvReadException {
        return resourceReader.readResource(resourceName);
    }
}