package ru.otus.spring.dao;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.domain.Question;
import ru.otus.spring.exception.CsvReadException;
import ru.otus.spring.props.TestFileNameProvider;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static ru.otus.spring.utils.TestConstants.*;

@ExtendWith(MockitoExtension.class)
class CsvQuestionDaoTest {

    @Mock
    private TestFileNameProvider propConfig;

    @InjectMocks
    private CsvQuestionDao csvQuestionDao;

    @Test
    void assertNotNullTestBeans() {
        assertNotNull(propConfig);
        assertNotNull(csvQuestionDao);
    }

    @Test
    void assertReturnOneQuestionByNullCsvPath() {
        assertEquals(NULL_CSV_PATH_MESSAGE,
                assertThrows(CsvReadException.class, () -> csvQuestionDao.getAll()).getMessage());
    }

    @Test
    @Disabled("For checkstyle")
    void assertReturnEmptyQuestionListByEmptyCsvPath() {
        when(propConfig.getTestFileName()).then(invocationOnMock -> StringUtils.EMPTY);
        assertDoesNotThrowAndReturnResult(Collections.emptyList());
    }

    @Test
    void assertReturnOneQuestionByInvalidCsvPath() {
        when(propConfig.getTestFileName()).then(invocationOnMock -> INVALID_CSV_PATH);
        assertEquals("CSV parsing stopped with an error: class path resource ["
                        + INVALID_CSV_PATH
                        + "] cannot be opened because it does not exist",
                assertThrows(CsvReadException.class, () -> csvQuestionDao.getAll()).getMessage());
    }

    @Test
    void assertReturnEmptyQuestionListByEmptyCsv() {
        when(propConfig.getTestFileName()).then(invocationOnMock -> EMPTY_CSV_PATH);
        assertDoesNotThrowAndReturnResult(Collections.emptyList());
    }

    @Test
    void assertReturnOneQuestionByNoDelimiterCsv() {
        when(propConfig.getTestFileName()).then(invocationOnMock -> INCORRECT_CSV_PATH);
        assertEquals(COLUMN_INDEX_OUT_OF_BOUNDS_ERROR_MESSAGE,
                assertThrows(CsvReadException.class, () -> csvQuestionDao.getAll()).getMessage());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/" + GOOD_CSV_PATH)
    void assertReturnListByGoodCsv(String firstElement, String secondElement) {
        when(propConfig.getTestFileName()).then(invocationOnMock -> GOOD_CSV_PATH);
        Question question = Question.builder()
                .text(firstElement)
                .rightAnswer(secondElement)
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