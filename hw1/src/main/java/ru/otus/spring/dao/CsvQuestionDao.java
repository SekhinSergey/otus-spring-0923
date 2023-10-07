package ru.otus.spring.dao;

import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;
import ru.otus.spring.exception.CsvReadException;
import ru.otus.spring.util.ResourceReader;

import java.util.ArrayList;
import java.util.List;

public class CsvQuestionDao implements QuestionDao {

    private final String resourceName;

    private final ResourceReader resourceReader;

    public CsvQuestionDao(String resourceName, ResourceReader resourceReader) {
        this.resourceName = resourceName;
        this.resourceReader = resourceReader;
    }

    public List<Question> getAll() throws CsvReadException {
        List<List<String>> csvList = resourceReader.readResource(resourceName);
        return buildQuestions(csvList);
    }

    private static List<Question> buildQuestions(List<List<String>> csvList) {
        List<Question> questions = new ArrayList<>();
        for (List<String> list : csvList) {
            Answer studentAnswer = new Answer(list.get(1));
            Answer goodAnswer = new Answer(list.get(2));
            Question question = Question.builder()
                    .text(list.get(0))
                    .studentAnswer(studentAnswer)
                    .goodAnswer(goodAnswer)
                    .isRight(new Answer(studentAnswer.equals(goodAnswer)))
                    .build();
            questions.add(question);
        }
        return questions;
    }
}
