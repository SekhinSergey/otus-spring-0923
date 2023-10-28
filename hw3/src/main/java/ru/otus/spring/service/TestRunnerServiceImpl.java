package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.TestResult;
import ru.otus.spring.domain.User;

@Component
@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private final UserService userService;

    private final TestService testService;

    private final ResultService resultService;

    public void run() {
        User user = userService.getUser();
        TestResult testResult = testService.testStudent(user);
        resultService.printResult(testResult);
    }
}
