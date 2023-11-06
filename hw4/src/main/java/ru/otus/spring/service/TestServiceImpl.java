package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.TestResult;
import ru.otus.spring.domain.User;
import ru.otus.spring.io.LocalizedIOService;

import static ru.otus.spring.domain.TestResult.checkTheAnswer;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private static final String TITLE = "TestService.title";

    private final LocalizedIOService localizedIoService;

    private final QuestionDao questionDao;

    public TestResult testStudent(User user) {
        prepareTest();
        int rightAnswerCount = 0;
        TestResult testResult = new TestResult(user);
        for (Question question : questionDao.getAll()) {
            ask(question);
            String studentAnswer = localizedIoService.readLine();
            rightAnswerCount = checkTheAnswer(rightAnswerCount, testResult, question, studentAnswer);
        }
        testResult.setRightAnswerCount(rightAnswerCount);
        return testResult;
    }

    private void prepareTest() {
        localizedIoService.skipPrintLn();
        localizedIoService.skipPrintLn();
        localizedIoService.localizedPrintLn(TITLE);
        localizedIoService.skipPrintLn();
    }

    private void ask(Question question) {
        String questionText = question.getText();
        localizedIoService.printLn(questionText);
    }
}
