package ru.otus.spring.service;

import ru.otus.spring.domain.TestResult;
import ru.otus.spring.domain.User;

public interface TestService {

    TestResult testStudent(User user);
}
