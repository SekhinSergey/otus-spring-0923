package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.TestResult;
import ru.otus.spring.domain.User;

@Component
@RequiredArgsConstructor
public class TestCommandLineRunner implements CommandLineRunner {

    private final UserService userService;

    private final TestService testService;

    private final ResultService resultService;

    public void run(String... args) {
        User user = userService.getUser();
        TestResult testResult = testService.testStudent(user);
        resultService.printResult(testResult);
    }
}
