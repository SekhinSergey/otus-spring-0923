package ru.otus.spring.dao;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.domain.Question;
import ru.otus.spring.exception.CsvReadException;
import ru.otus.spring.props.AppProps;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CsvQuestionDaoTest {

    private static final String NULL_CSV_PATH_MESSAGE = "CSV parsing stopped with an error: Path must not be null";

    private static final String INVALID_CSV_PATH = "1.csv";

    private static final String EMPTY_CSV_PATH = "empty.csv";

    private static final String INCORRECT_CSV_PATH = "incorrect.csv";

    private static final String COLUMN_INDEX_OUT_OF_BOUNDS_ERROR_MESSAGE = "Column with index 1 not found in CSV";

    private static final String GOOD_CSV_PATH = "good.csv";

    @Mock
    private AppProps appProps;

    @InjectMocks
    private CsvQuestionDao csvQuestionDao;

    @Test
    void assertNotNullTestBeans() {
        assertNotNull(appProps);
        assertNotNull(csvQuestionDao);
    }

    @Test
    void assertReturnOneQuestionByNullCsvPath() {
        assertEquals(NULL_CSV_PATH_MESSAGE,
                assertThrows(CsvReadException.class, () -> csvQuestionDao.getAll()).getMessage());
    }

    @Test
    void assertReturnEmptyQuestionListByEmptyCsvPath() {
        when(appProps.getProperty()).then(invocationOnMock -> StringUtils.EMPTY);
        assertDoesNotThrowAndReturnResult(Collections.emptyList());
    }

    @Test
    void assertReturnOneQuestionByInvalidCsvPath() {
        when(appProps.getProperty()).then(invocationOnMock -> INVALID_CSV_PATH);
        assertEquals("CSV parsing stopped with an error: class path resource ["
                        + INVALID_CSV_PATH
                        + "] cannot be opened because it does not exist",
                assertThrows(CsvReadException.class, () -> csvQuestionDao.getAll()).getMessage());
    }

    @Test
    void assertReturnEmptyQuestionListByEmptyCsv() {
        when(appProps.getProperty()).then(invocationOnMock -> EMPTY_CSV_PATH);
        assertDoesNotThrowAndReturnResult(Collections.emptyList());
    }

    @Test
    void assertReturnOneQuestionByNoDelimiterCsv() {
        when(appProps.getProperty()).then(invocationOnMock -> INCORRECT_CSV_PATH);
        assertEquals(COLUMN_INDEX_OUT_OF_BOUNDS_ERROR_MESSAGE,
                assertThrows(CsvReadException.class, () -> csvQuestionDao.getAll()).getMessage());
    }

    @Test
    void assertReturnListByGoodCsv() {
        when(appProps.getProperty()).then(invocationOnMock -> GOOD_CSV_PATH);
        Question question = Question.builder()
                .text("1")
                .rightAnswer("2")
                .build();
        assertDoesNotThrowAndReturnResult(Collections.singletonList(question));
    }

    private void assertDoesNotThrowAndReturnResult(List<Question> expected) {
        assertDoesNotThrow(() -> {
            csvQuestionDao.getAll();
        });
        assertEquals(expected, csvQuestionDao.getAll());
    }
}