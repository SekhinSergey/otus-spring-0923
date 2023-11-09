package ru.otus.spring.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.domain.TestResult;
import ru.otus.spring.domain.User;
import ru.otus.spring.service.ResultService;
import ru.otus.spring.service.TestService;
import ru.otus.spring.service.UserService;

@ShellComponent
@RequiredArgsConstructor
public class TestShellCommands {

    private final UserService userService;

    private final TestService testService;

    private final ResultService resultService;

    @ShellMethod(value = "Test running command", key = {"r", "run"})
    public void run() {
        User user = userService.getUser();
        TestResult testResult = testService.testStudent(user);
        resultService.printResult(testResult);
    }
}
