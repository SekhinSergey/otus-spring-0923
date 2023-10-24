package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.TestResult;
import ru.otus.spring.io.IOService;
import ru.otus.spring.io.InteractiveService;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {

    private static final String STUDENT_CORRECT_ANSWERS_NUMBER_MESSAGE = "Number of correct answers by student ";

    private static final String UNANSWERED_QUESTIONS_REQUEST_MESSAGE = "Do you want to see unanswered questions?";

    private static final String RIGHT_ANSWER_REQUEST_MESSAGE = "Do you want to see right answer?";


    private final IOService ioService;

    private final InteractiveService interactiveService;

    public void printResult(TestResult testResult) {
        printBaseTestResults(testResult);
        printDetailedTestResults(testResult);
    }

    private void printBaseTestResults(TestResult testResult) {
        ioService.printLn(STUDENT_CORRECT_ANSWERS_NUMBER_MESSAGE +
                testResult.getUser().getFullName() + ": " +
                testResult.getRightAnswerCount());
    }

    private void printDetailedTestResults(TestResult testResult) {
        ioService.printLn(UNANSWERED_QUESTIONS_REQUEST_MESSAGE);
        interactiveService.printConfirmationRequest();
        if (interactiveService.isOngoing()) {
            testResult.getUnansweredQuestions().forEach(question -> {
                ioService.printLn(question.getText());
                ioService.printLn(RIGHT_ANSWER_REQUEST_MESSAGE);
                interactiveService.printConfirmationRequest();
                if (interactiveService.isOngoing()) {
                    ioService.printLn(question.getRightAnswer());
                }
            });
        }
    }
}
