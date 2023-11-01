package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.TestResult;
import ru.otus.spring.io.LocalizedIOService;
import ru.otus.spring.props.TestConfig;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {

    private static final String RESULT = "ResultService.result.message";

    private static final String CONGRATULATORY_MESSAGE = "LocalizedResultIOService.congratulatory.message";

    private static final String FAILURE_MESSAGE = "LocalizedResultIOService.failure.message";

    private static final String UNANSWERED_QUESTIONS_REQUEST_MESSAGE
            = "ResultService.unanswered.questions.request.message";

    private static final String RIGHT_ANSWER_REQUEST_MESSAGE = "ResultService.right.answer.request.message";

    private final LocalizedIOService localizedIoService;

    private final TestConfig testConfig;

    public void printResult(TestResult testResult) {
        printBaseTestResults(testResult);
        if (!isSuccess(testResult)) {
            printDetailedTestResults(testResult);
        }
    }

    private void printBaseTestResults(TestResult testResult) {
        localizedIoService.formattedLocalizedPrintLn(RESULT, testResult.getUser().getFullName());
        int rightAnswerCount = testResult.getRightAnswerCount();
        localizedIoService.printLn(rightAnswerCount);
        if (isSuccess(testResult)) {
            localizedIoService.localizedPrintLn(CONGRATULATORY_MESSAGE);
        } else if (rightAnswerCount < testConfig.getMinResult()) {
            localizedIoService.localizedPrintLn(FAILURE_MESSAGE);
        }
    }

    private void printDetailedTestResults(TestResult testResult) {
        localizedIoService.localizedPrintLn(UNANSWERED_QUESTIONS_REQUEST_MESSAGE);
        localizedIoService.confirmationRequestPrintLn();
        if (localizedIoService.isOngoing()) {
            testResult.getUnansweredQuestions().forEach(question -> {
                localizedIoService.printLn(question.getText());
                localizedIoService.localizedPrintLn(RIGHT_ANSWER_REQUEST_MESSAGE);
                localizedIoService.confirmationRequestPrintLn();
                if (localizedIoService.isOngoing()) {
                    localizedIoService.printLn(question.getRightAnswer());
                }
            });
        }
    }

    private static boolean isSuccess(TestResult testResult) {
        return testResult.getUnansweredQuestions().isEmpty();
    }
}
