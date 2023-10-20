package ru.otus.spring.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.config.*;
import ru.otus.spring.domain.Question;
import ru.otus.spring.exception.CsvReadException;
import ru.otus.spring.props.ResourceProvider;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.otus.spring.utils.TestConstants.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        NullCsvResourceProvider.class,
        EmptyCsvPathResourceProvider.class,
        InvalidCsvPathResourceProvider.class,
        EmptyCsvResourceProvider.class,
        IncorrectCsvResourceProvider.class,
        GoodCsvResourceProvider.class})
class CsvQuestionDaoIntegrationTest {

    @Autowired
    @Qualifier("nullCsvResourceProvider")
    private ResourceProvider nullCsvResourceProvider;

    @Autowired
    @Qualifier("emptyCsvPathResourceProvider")
    private ResourceProvider emptyCsvPathResourceProvider;

    @Autowired
    @Qualifier("invalidCsvPathResourceProvider")
    private ResourceProvider invalidCsvPathResourceProvider;

    @Autowired
    @Qualifier("emptyCsvResourceProvider")
    private ResourceProvider emptyCsvResourceProvider;

    @Autowired
    @Qualifier("incorrectCsvResourceProvider")
    private ResourceProvider incorrectCsvResourceProvider;

    @Autowired
    @Qualifier("goodCsvResourceProvider")
    private ResourceProvider goodCsvResourceProvider;

    @Test
    void assertNotNullTestBeans() {
        assertNotNull(nullCsvResourceProvider);
        assertNotNull(emptyCsvPathResourceProvider);
        assertNotNull(invalidCsvPathResourceProvider);
        assertNotNull(emptyCsvResourceProvider);
        assertNotNull(incorrectCsvResourceProvider);
        assertNotNull(goodCsvResourceProvider);
    }

    @Test
    void assertReturnOneQuestionByNullCsvReader() {
        QuestionDao questionDao = new CsvQuestionDao(null);
        assertEquals(NULL_CSV_READER_MESSAGE,
                assertThrows(CsvReadException.class, questionDao::getAll).getMessage());
    }

    @Test
    void assertReturnOneQuestionByNullCsvPath() {
        QuestionDao questionDao = new CsvQuestionDao(nullCsvResourceProvider);
        assertEquals(NULL_CSV_PATH_MESSAGE,
                assertThrows(CsvReadException.class, questionDao::getAll).getMessage());
    }

    @Test
    void assertReturnEmptyQuestionListByEmptyCsvPath() {
        QuestionDao questionDao = new CsvQuestionDao(emptyCsvPathResourceProvider);
        assertDoesNotThrowAndReturnResult(questionDao, Collections.emptyList());
    }

    @Test
    void assertReturnOneQuestionByInvalidCsvPath() {
        QuestionDao questionDao = new CsvQuestionDao(invalidCsvPathResourceProvider);
        assertEquals("CSV parsing stopped with an error: class path resource ["
                        + INVALID_CSV_PATH
                        + "] cannot be opened because it does not exist",
                assertThrows(CsvReadException.class, questionDao::getAll).getMessage());
    }

    @Test
    void assertReturnEmptyQuestionListByEmptyCsv() {
        QuestionDao questionDao = new CsvQuestionDao(emptyCsvResourceProvider);
        assertDoesNotThrowAndReturnResult(questionDao, Collections.emptyList());
    }

    @Test
    void assertReturnOneQuestionByNoDelimiterCsv() {
        QuestionDao questionDao = new CsvQuestionDao(incorrectCsvResourceProvider);
        assertEquals(COLUMN_INDEX_OUT_OF_BOUNDS_ERROR_MESSAGE,
                assertThrows(CsvReadException.class, questionDao::getAll).getMessage());
    }

    @Test
    void assertReturnListByGoodCsv() {
        QuestionDao questionDao = new CsvQuestionDao(goodCsvResourceProvider);
        Question question = Question.builder()
                .text("1")
                .rightAnswer("2")
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
