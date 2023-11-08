package ru.otus.spring.service;

import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.TestResult;
import ru.otus.spring.domain.User;
import ru.otus.spring.io.*;
import ru.otus.spring.props.LocaleConfig;
import ru.otus.spring.props.TestConfig;
import ru.otus.spring.props.TestFileNameProvider;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static ru.otus.spring.utils.TestConstants.*;

@Getter
@SpringBootTest
@ContextConfiguration(classes = TestIntegrationTest.TestTestConfiguration.class)
class TestIntegrationTest {

    private static final String NAME = "Name";

    private static final String BAD_ANSWER = "1";

    private static final String RIGHT_ANSWER = "2";

    private static final int ZERO_RIGHT_ANSWER = 0;

    private static final int ONE_RIGHT_ANSWER = 1;

    @TestConfiguration
    static class TestTestConfiguration implements TestFileNameProvider {

        @Override
        public String getTestFileName() {
            return GOOD_CSV_PATH;
        }
    }

    @MockBean
    private LocaleConfig localeConfig;

    @MockBean
    private TestConfig testConfig;

    @MockBean
    private LocalizedIOService localizedIoService;

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private TestService testService;

    @Autowired
    private ResultService resultService;

    @ParameterizedTest
    @CsvFileSource(resources = "/" + GOOD_CSV_PATH)
    void assertFailedTestResult(String firstElement, String secondElement) {
        User user = testUser();

        TestResult testResult = testService.testStudent(user);
        int rightAnswerCount = testResult.getRightAnswerCount();
        assertEquals(ZERO_RIGHT_ANSWER, rightAnswerCount);
        Question question = buildTestQuestion(firstElement, secondElement);
        List<Question> unansweredQuestions = testResult.getUnansweredQuestions();
        assertEquals(Collections.singletonList(question), unansweredQuestions);

        when(testConfig.getMinResult()).thenReturn(3);
        when(localizedIoService.isOngoing()).thenReturn(true);
        assertTrue(isPrintResultSuccessful(testResult));
    }

    @Test
    void assertGoodTestResult() {
        User user = testUser();

        when(localizedIoService.readLine()).thenReturn(RIGHT_ANSWER);
        TestResult testResult = testService.testStudent(user);
        assertEquals(user, testResult.getUser());
        int rightAnswerCount = testResult.getRightAnswerCount();
        assertEquals(ONE_RIGHT_ANSWER, rightAnswerCount);
        List<Question> unansweredQuestions = testResult.getUnansweredQuestions();
        assertEquals(Collections.emptyList(), unansweredQuestions);

        assertTrue(isPrintResultSuccessful(testResult));
    }

    private User testUser() {
        when(localizedIoService.readLine()).thenReturn(NAME);
        User user = userService.getUser();
        assertEquals(buildTestUser(), user);
        return user;
    }

    private User buildTestUser() {
        User user = new User();
        user.setFirstName(NAME);
        user.setSurname(NAME);
        return user;
    }

    private Question buildTestQuestion(String firstElement, String secondElement) {
        return Question.builder()
                .text(firstElement)
                .rightAnswer(secondElement)
                .build();
    }

    private boolean isPrintResultSuccessful(TestResult testResult) {
        resultService.printResult(testResult);
        return true;
    }
}
