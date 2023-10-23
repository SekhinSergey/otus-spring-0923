package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.TestResult;
import ru.otus.spring.out.IOService;
import ru.otus.spring.out.InteractiveService;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {

    private final IOService ioService;

    private final InteractiveService interactiveService;

    public void printResult(TestResult testResult) {
        printBaseTestResults(testResult);
        printDetailedTestResults(testResult);
    }

    private void printBaseTestResults(TestResult testResult) {
        ioService.printLn("Number of correct answers by student " +
                testResult.getUser().getFullName() + ": " +
                testResult.getRightAnswerCount());
    }

    private void printDetailedTestResults(TestResult testResult) {
        ioService.printLn("Do you want to see unanswered questions?");
        interactiveService.printConfirmationRequest();
        if (interactiveService.isOngoing()) {
            testResult.getUnansweredQuestions().forEach(question -> {
                ioService.printLn(question.getText());
                ioService.printLn("Do you want to see right answer?");
                interactiveService.printConfirmationRequest();
                if (interactiveService.isOngoing()) {
                    ioService.printLn(question.getRightAnswer());
                }
            });
        }
    }
}
