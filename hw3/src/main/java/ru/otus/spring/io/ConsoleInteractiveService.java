package ru.otus.spring.io;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConsoleInteractiveService implements InteractiveService {

    private static final String CONFIRMATION_REQUEST_MESSAGE = "Enter \"Yes\" or \"No\"?";

    private static final String APPLY_MESSAGE = "Yes";

    private final IOService ioService;

    public void printConfirmationRequest() {
        ioService.printLn(CONFIRMATION_REQUEST_MESSAGE);
    }

    public boolean isOngoing() {
        return ioService.readLine().equals(APPLY_MESSAGE);
    }
}
