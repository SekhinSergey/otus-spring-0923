package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.User;
import ru.otus.spring.exception.CsvReadException;
import ru.otus.spring.out.Printer;

import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private static final String TEST_TITLE = "Testing students";

    private final QuestionDao questionDao;

    private final Printer printer;

    private final UserService userService;

    public void printTest() throws CsvReadException {
        User user = userService.getUser();
        prepareTest();
        int rightAnswerCount = testStudents();
        printTestResult(user, rightAnswerCount);
    }

    private void prepareTest() {
        printer.skipLine();
        printer.skipLine();
        printer.print(TEST_TITLE);
        printer.skipLine();
    }

    private int testStudents() {
        Scanner scanner = new Scanner(System.in);
        int rightAnswerCount = 0;
        for (Question question : questionDao.getAll()) {
            String questionText = question.getText();
            printer.print(questionText);
            String studentAnswer = scanner.next();
            String rightAnswer = question.getRightAnswer();
            if (rightAnswer.equals(studentAnswer)) {
                rightAnswerCount++;
            }
        }
        return rightAnswerCount;
    }

    private void printTestResult(User user, int rightAnswerCount) {
        printer.print("Number of correct answers by student " + user.getFullName() + ": " + rightAnswerCount);
    }
}
