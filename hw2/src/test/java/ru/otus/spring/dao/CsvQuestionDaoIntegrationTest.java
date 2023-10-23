package ru.otus.spring.dao;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.domain.Question;
import ru.otus.spring.exception.CsvReadException;
import ru.otus.spring.props.ResourceProvider;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.otus.spring.utils.TestConstants.*;

@ExtendWith(SpringExtension.class)
@PropertySource("/csv-test.properties")
@ContextConfiguration(classes = CsvQuestionDaoIntegrationTest.class)
class CsvQuestionDaoIntegrationTest {

    @Value("${question-good.source}")
    private String goodCsvResourcePath;

    @Test
    void assertReturnOneQuestionByNullCsvReader() {
        QuestionDao questionDao = new CsvQuestionDao(null);
        assertEquals(NULL_CSV_READER_MESSAGE,
                assertThrows(CsvReadException.class, questionDao::getAll).getMessage());
    }

    @Test
    void assertReturnOneQuestionByNullCsvPath() {
        ResourceProvider resourceProvider = () -> null;
        QuestionDao questionDao = new CsvQuestionDao(resourceProvider);
        assertEquals(NULL_CSV_PATH_MESSAGE,
                assertThrows(CsvReadException.class, questionDao::getAll).getMessage());
    }

    @Test
    void assertReturnEmptyQuestionListByEmptyCsvPath() {
        ResourceProvider resourceProvider = () -> StringUtils.EMPTY;
        QuestionDao questionDao = new CsvQuestionDao(resourceProvider);
        assertDoesNotThrowAndReturnResult(questionDao, Collections.emptyList());
    }

    @Test
    void assertReturnOneQuestionByInvalidCsvPath() {
        ResourceProvider resourceProvider = () -> INVALID_CSV_PATH;
        QuestionDao questionDao = new CsvQuestionDao(resourceProvider);
        assertEquals("CSV parsing stopped with an error: class path resource ["
                        + INVALID_CSV_PATH
                        + "] cannot be opened because it does not exist",
                assertThrows(CsvReadException.class, questionDao::getAll).getMessage());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/" + GOOD_CSV_PATH)
    void assertReturnListByGoodCsv(String firstElement, String secondElement) {
        ResourceProvider resourceProvider = () -> goodCsvResourcePath;
        QuestionDao questionDao = new CsvQuestionDao(resourceProvider);
        Question question = Question.builder()
                .text(firstElement)
                .rightAnswer(secondElement)
                .build();
        assertDoesNotThrowAndReturnResult(questionDao, Collections.singletonList(question));
    }

    private void assertDoesNotThrowAndReturnResult(QuestionDao questionDao, List<Question> expected) {
        assertDoesNotThrow(() -> {
            questionDao.getAll();
        });
        assertEquals(expected, questionDao.getAll());
    }
}
