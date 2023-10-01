package ru.otus.spring.service;

import ru.otus.spring.dao.SurveyDao;
import ru.otus.spring.domain.Survey;
import ru.otus.spring.exception.CsvReadException;

import java.util.ArrayList;
import java.util.List;

public class SurveyServiceImpl implements SurveyService {

    private final SurveyDao surveyDao;

    public SurveyServiceImpl(SurveyDao surveyDao) {
        this.surveyDao = surveyDao;
    }

    public List<Survey> getSurveys() throws CsvReadException {
        List<List<String>> lists = surveyDao.parseCsvToSurvey();
        List<Survey> surveys = new ArrayList<>();
        for (List<String> list : lists) {
            Survey survey = Survey.builder()
                    .question(list.get(0))
                    .badAnswer(list.get(1))
                    .goodAnswer(list.get(2))
                    .build();
            surveys.add(survey);
        }
        return surveys;
    }
}