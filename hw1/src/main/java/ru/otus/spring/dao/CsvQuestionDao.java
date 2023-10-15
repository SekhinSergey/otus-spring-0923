package ru.otus.spring.dao;

import org.springframework.core.io.ClassPathResource;
import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;
import ru.otus.spring.exception.CsvReadException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvQuestionDao implements QuestionDao {

    private final String resourceName;

    public CsvQuestionDao(String resourceName) {
        this.resourceName = resourceName;
    }

    public List<Question> getAll() throws CsvReadException {
        List<List<String>> csvList = readResource();
        return buildQuestions(csvList);
    }

    public List<List<String>> readResource() throws CsvReadException {
        List<List<String>> csvList = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                new ClassPathResource(resourceName).getInputStream()))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");
                csvList.add(Arrays.asList(values));
            }
        } catch (Exception exception) {
            throw new CsvReadException("CSV parsing stopped with an error: " + exception.getMessage());
        }
        return csvList;
    }

    private static List<Question> buildQuestions(List<List<String>> csvList) {
        List<Question> questions = new ArrayList<>();
        for (List<String> list : csvList) {
            int columnIndex = 0;
            try {
                Answer studentAnswer = new Answer(list.get(++columnIndex));
                Answer goodAnswer = new Answer(list.get(++columnIndex));
                Question question = Question.builder()
                        .text(list.get(0))
                        .studentAnswer(studentAnswer)
                        .goodAnswer(goodAnswer)
                        .isRight(new Answer(studentAnswer.equals(goodAnswer)))
                        .build();
                questions.add(question);
            } catch (Exception exception) {
                throw new CsvReadException("Column with index " + columnIndex + " not found in CSV");
            }
        }
        return questions;
    }
}
