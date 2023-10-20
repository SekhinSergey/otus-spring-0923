package ru.otus.spring.service;

import ru.otus.spring.domain.User;

public interface StudentTestResultService {

    void printResult(User user, int rightAnswerCount);
}
