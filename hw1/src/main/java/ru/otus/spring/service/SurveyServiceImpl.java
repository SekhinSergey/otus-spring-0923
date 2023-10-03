package ru.otus.spring.service;

import ru.otus.spring.dao.SurveyDao;
import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.Survey;
import ru.otus.spring.exception.CsvReadException;
import ru.otus.spring.out.Printer;

import java.util.ArrayList;
import java.util.List;

public class SurveyServiceImpl implements SurveyService {

    private final SurveyDao surveyDao;

    private final Printer printer;

    public SurveyServiceImpl(SurveyDao surveyDao, Printer printer) {
        this.surveyDao = surveyDao;
        this.printer = printer;
    }

    public List<Survey> getSurveys() throws CsvReadException {
        List<List<String>> lists = surveyDao.parseCsvToSurvey();
        List<Survey> surveys = new ArrayList<>();
        for (List<String> list : lists) {
            Survey survey = Survey.builder()
                    .question(new Question(list.get(0)))
                    .badAnswer(new Answer(list.get(1)))
                    .goodAnswer(new Answer(list.get(2)))
                    .build();
            surveys.add(survey);
        }
        for (Survey survey : surveys) {
            printer.print(survey);
        }
        return surveys;
    }
}
