package ru.otus.spring.io;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocalizedIOServiceImpl implements LocalizedIOService {

    private static final String CONFIRMATION_REQUEST_MESSAGE = "LocalizedIOService.confirmation.request.message";

    private static final String APPLY_MESSAGE = "LocalizedIOService.apply.message";

    private final ResourceBundleReader resourceBundleReader;

    private final IOService ioService;

    public void printLn(Object o) {
        ioService.printLn(o);
    }

    public void skipPrintLn() {
        ioService.skipPrintLn();
    }

    public String readLine() {
        return ioService.readLine();
    }

    public void localizedPrintLn(String propertyKey) {
        String message = resourceBundleReader.getMessage(propertyKey);
        ioService.printLn(message);
    }

    public void formattedLocalizedPrintLn(String propertyKey, Object... args) {
        String messageWithArgs = resourceBundleReader.getMessageWithArgs(propertyKey, args);
        ioService.printLn(messageWithArgs);
    }

    public void confirmationRequestPrintLn() {
        String message = resourceBundleReader.getMessage(CONFIRMATION_REQUEST_MESSAGE);
        ioService.printLn(message);
    }

    public boolean isOngoing() {
        String message = resourceBundleReader.getMessage(APPLY_MESSAGE);
        return ioService.readLine().equals(message);
    }
}
