package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.TestResult;
import ru.otus.spring.domain.User;
import ru.otus.spring.exception.CsvReadException;
import ru.otus.spring.io.IOService;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private static final String TEST_TITLE = "Testing students";

    private final UserService userService;

    private final IOService ioService;

    private final QuestionDao questionDao;

    private final ResultService resultService;

    public void printTest() throws CsvReadException {
        User user = userService.getUser();
        prepareTest();
        TestResult testResult = testStudents(user);
        resultService.printResult(testResult);
    }

    private void prepareTest() {
        ioService.skipPrintLn();
        ioService.skipPrintLn();
        ioService.printLn(TEST_TITLE);
        ioService.skipPrintLn();
    }

    private TestResult testStudents(User user) {
        int rightAnswerCount = 0;
        TestResult testResult = new TestResult(user);
        for (Question question : questionDao.getAll()) {
            String questionText = question.getText();
            ioService.printLn(questionText);
            String studentAnswer = ioService.readLine();
            String rightAnswer = question.getRightAnswer();
            if (rightAnswer.equals(studentAnswer)) {
                rightAnswerCount++;
            } else {
                testResult.getUnansweredQuestions().add(question);
            }
        }
        testResult.setRightAnswerCount(rightAnswerCount);
        return testResult;
    }
}
