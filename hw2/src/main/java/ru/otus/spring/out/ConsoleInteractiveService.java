package ru.otus.spring.out;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConsoleInteractiveService implements InteractiveService {

    private final IOService ioService;

    public void printConfirmationRequest() {
        ioService.printLn("Enter \"Yes\" or \"No\"?");
    }

    public boolean isOngoing() {
        return ioService.readLine().equals("Yes");
    }
}
