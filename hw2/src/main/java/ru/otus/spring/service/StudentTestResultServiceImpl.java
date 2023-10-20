package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.User;
import ru.otus.spring.out.IOService;

@Service
@RequiredArgsConstructor
public class StudentTestResultServiceImpl implements StudentTestResultService {

    private final IOService ioService;

    public void printResult(User user, int rightAnswerCount) {
        ioService.printLn("Number of correct answers by student " + user.getFullName() + ": " + rightAnswerCount);
    }
}
