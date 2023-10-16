package ru.otus.spring.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Question;
import ru.otus.spring.exception.CsvReadException;
import ru.otus.spring.props.AppProps;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {

    private final AppProps appProps;

    public List<Question> getAll() throws CsvReadException {
        List<List<String>> csvList = readResource();
        return buildQuestions(csvList);
    }

    public List<List<String>> readResource() throws CsvReadException {
        List<List<String>> csvList = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                new ClassPathResource(appProps.getProperty()).getInputStream()))) {
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
            int columnIndex = -1;
            try {
                String questionText = list.get(++columnIndex);
                String rightAnswer = list.get(++columnIndex);
                Question question = Question.builder()
                        .text(questionText)
                        .rightAnswer(rightAnswer)
                        .build();
                questions.add(question);
            } catch (Exception exception) {
                throw new CsvReadException("Column with index " + columnIndex + " not found in CSV");
            }
        }
        return questions;
    }
}
