package ru.otus.spring.service;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import ru.otus.spring.dao.CsvQuestionDao;
import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.TestResult;
import ru.otus.spring.domain.User;
import ru.otus.spring.exception.CsvReadException;
import ru.otus.spring.io.*;
import ru.otus.spring.props.TestFileNameProvider;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.otus.spring.utils.TestConstants.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "/csv-test.properties")
class TestServiceIntegrationTest {

    private static final String FIRST_NAME = "Sergey";

    private static final String SURNAME = "Sekhin";

    private static final String BAD_ANSWER = "1";

    private static final String RIGHT_ANSWER = "2";

    private static final int ZERO_RIGHT_ANSWER = 0;

    private static final int ONE_RIGHT_ANSWER = 1;

    @Value("${question-good.source}")
    private String goodCsvResourcePath;

    @Autowired
    private ResourceBundleReader resourceBundleReader;

    @Test
    void assertReturnOneQuestionByNullCsvReader() {
        QuestionDao questionDao = new CsvQuestionDao(null);
        assertEquals(NULL_CSV_READER_MESSAGE,
                assertThrows(CsvReadException.class, questionDao::getAll).getMessage());
    }

    @Test
    void assertReturnOneQuestionByNullCsvPath() {
        TestFileNameProvider testFileNameProvider = () -> null;
        QuestionDao questionDao = new CsvQuestionDao(testFileNameProvider);
        assertEquals(NULL_CSV_PATH_MESSAGE,
                assertThrows(CsvReadException.class, questionDao::getAll).getMessage());
    }

    @Test
    @Disabled("For checkstyle")
    void assertReturnEmptyQuestionListByEmptyCsvPath() {
        QuestionDao questionDao = getQuestionDaoByFilePath(StringUtils.EMPTY);
        assertDoesNotThrowAndReturnResult(questionDao, Collections.emptyList());
    }

    @Test
    void assertReturnOneQuestionByInvalidCsvPath() {
        QuestionDao questionDao = getQuestionDaoByFilePath(INVALID_CSV_PATH);
        assertEquals("CSV parsing stopped with an error: class path resource ["
                        + INVALID_CSV_PATH
                        + "] cannot be opened because it does not exist",
                assertThrows(CsvReadException.class, questionDao::getAll).getMessage());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/" + GOOD_CSV_PATH)
    void assertReturnListByGoodCsv(String firstElement, String secondElement) {
        QuestionDao questionDao = getQuestionDaoByFilePath(goodCsvResourcePath);
        Question question = prepareQuestion(firstElement, secondElement);
        assertDoesNotThrowAndReturnResult(questionDao, Collections.singletonList(question));
    }

    private void assertDoesNotThrowAndReturnResult(QuestionDao questionDao, List<Question> expected) {
        assertDoesNotThrow(() -> {
            questionDao.getAll();
        });
        assertEquals(expected, questionDao.getAll());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/" + GOOD_CSV_PATH)
    void assertFailedTestResult(String firstElement, String secondElement) {
        User user = prepareUser();
        TestServiceImpl testService = prepareTestResult(BAD_ANSWER);

        TestResult testResult = testService.testStudent(user);

        assertEquals(user, testResult.getUser());

        int rightAnswerCount = testResult.getRightAnswerCount();
        assertEquals(ZERO_RIGHT_ANSWER, rightAnswerCount);

        Question question = prepareQuestion(firstElement, secondElement);
        List<Question> unansweredQuestions = testResult.getUnansweredQuestions();
        assertEquals(Collections.singletonList(question), unansweredQuestions);

    }

    @Test
    void assertGoodTestResult() {
        User user = prepareUser();
        TestServiceImpl testService = prepareTestResult(RIGHT_ANSWER);

        TestResult testResult = testService.testStudent(user);

        assertEquals(user, testResult.getUser());

        int rightAnswerCount = testResult.getRightAnswerCount();
        assertEquals(ONE_RIGHT_ANSWER, rightAnswerCount);

        List<Question> unansweredQuestions = testResult.getUnansweredQuestions();
        assertEquals(Collections.emptyList(), unansweredQuestions);

    }

    private User prepareUser() {
        User user = new User();
        user.setFirstName(FIRST_NAME);
        user.setSurname(SURNAME);
        return user;
    }

    private TestServiceImpl prepareTestResult(String answer) {
        LocalizedIOService localizedIoService = new LocalizedIOServiceImpl(resourceBundleReader, mock(IOService.class));
        when(localizedIoService.readLine()).then(invocationOnMock -> answer);
        QuestionDao questionDao = getQuestionDaoByFilePath(goodCsvResourcePath);
        return new TestServiceImpl(localizedIoService, questionDao);
    }

    private QuestionDao getQuestionDaoByFilePath(String filePath) {
        TestFileNameProvider testFileNameProvider = () -> filePath;
        return new CsvQuestionDao(testFileNameProvider);
    }

    private Question prepareQuestion(String firstElement, String secondElement) {
        return Question.builder()
                .text(firstElement)
                .rightAnswer(secondElement)
                .build();
    }
}
