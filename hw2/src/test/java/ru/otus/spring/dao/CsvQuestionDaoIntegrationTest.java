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
import ru.otus.spring.props.AppProps;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.otus.spring.utils.TestConstants.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        NullCsvProps.class,
        EmptyCsvPathProps.class,
        InvalidCsvPathProps.class,
        EmptyCsvProps.class,
        IncorrectCsvProps.class,
        GoodCsvProps.class})
class CsvQuestionDaoIntegrationTest {

    @Autowired
    @Qualifier("nullCsvProps")
    private AppProps nullCsvProps;

    @Autowired
    @Qualifier("emptyCsvPathProps")
    private AppProps emptyCsvPathProps;

    @Autowired
    @Qualifier("invalidCsvPathProps")
    private AppProps invalidCsvPathProps;

    @Autowired
    @Qualifier("emptyCsvProps")
    private AppProps emptyCsvProps;

    @Autowired
    @Qualifier("incorrectCsvProps")
    private AppProps incorrectCsvProps;

    @Autowired
    @Qualifier("goodCsvProps")
    private AppProps goodCsvProps;

    @Test
    void assertNotNullTestBeans() {
        assertNotNull(nullCsvProps);
        assertNotNull(emptyCsvPathProps);
        assertNotNull(invalidCsvPathProps);
        assertNotNull(emptyCsvProps);
        assertNotNull(incorrectCsvProps);
        assertNotNull(goodCsvProps);
    }

    @Test
    void assertReturnOneQuestionByNullCsvReader() {
        QuestionDao questionDao = new CsvQuestionDao(null);
        assertEquals(NULL_CSV_READER_MESSAGE,
                assertThrows(CsvReadException.class, questionDao::getAll).getMessage());
    }

    @Test
    void assertReturnOneQuestionByNullCsvPath() {
        QuestionDao questionDao = new CsvQuestionDao(nullCsvProps);
        assertEquals(NULL_CSV_PATH_MESSAGE,
                assertThrows(CsvReadException.class, questionDao::getAll).getMessage());
    }

    @Test
    void assertReturnEmptyQuestionListByEmptyCsvPath() {
        QuestionDao questionDao = new CsvQuestionDao(emptyCsvPathProps);
        assertDoesNotThrowAndReturnResult(questionDao, Collections.emptyList());
    }

    @Test
    void assertReturnOneQuestionByInvalidCsvPath() {
        QuestionDao questionDao = new CsvQuestionDao(invalidCsvPathProps);
        assertEquals("CSV parsing stopped with an error: class path resource ["
                        + INVALID_CSV_PATH
                        + "] cannot be opened because it does not exist",
                assertThrows(CsvReadException.class, questionDao::getAll).getMessage());
    }

    @Test
    void assertReturnEmptyQuestionListByEmptyCsv() {
        QuestionDao questionDao = new CsvQuestionDao(emptyCsvProps);
        assertDoesNotThrowAndReturnResult(questionDao, Collections.emptyList());
    }

    @Test
    void assertReturnOneQuestionByNoDelimiterCsv() {
        QuestionDao questionDao = new CsvQuestionDao(incorrectCsvProps);
        assertEquals(COLUMN_INDEX_OUT_OF_BOUNDS_ERROR_MESSAGE,
                assertThrows(CsvReadException.class, questionDao::getAll).getMessage());
    }

    @Test
    void assertReturnListByGoodCsv() {
        QuestionDao questionDao = new CsvQuestionDao(goodCsvProps);
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
