package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.TestResult;
import ru.otus.spring.domain.User;
import ru.otus.spring.io.IOService;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private static final String TEST_TITLE = "Testing students";

    private final IOService ioService;

    private final QuestionDao questionDao;

    public TestResult testStudent(User user) {
        prepareTest();
        int rightAnswerCount = 0;
        TestResult testResult = new TestResult(user);
        for (Question question : questionDao.getAll()) {
            ask(question);
            String studentAnswer = ioService.readLine();
            rightAnswerCount = checkTheAnswer(rightAnswerCount, testResult, question, studentAnswer);
        }
        testResult.setRightAnswerCount(rightAnswerCount);
        return testResult;
    }

    private void prepareTest() {
        ioService.skipPrintLn();
        ioService.skipPrintLn();
        ioService.printLn(TEST_TITLE);
        ioService.skipPrintLn();
    }

    private void ask(Question question) {
        String questionText = question.getText();
        ioService.printLn(questionText);
    }

    private static int checkTheAnswer(int rightAnswerCount,
                                      TestResult testResult,
                                      Question question,
                                      String studentAnswer) {
        String rightAnswer = question.getRightAnswer();
        if (rightAnswer.equals(studentAnswer)) {
            rightAnswerCount++;
        } else {
            testResult.getUnansweredQuestions().add(question);
        }
        return rightAnswerCount;
    }
}
