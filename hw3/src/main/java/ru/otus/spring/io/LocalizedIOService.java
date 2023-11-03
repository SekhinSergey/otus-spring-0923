package ru.otus.spring.io;

public interface LocalizedIOService extends IOService {

    void localizedPrintLn(String propertyKey);

    void formattedLocalizedPrintLn(String propertyKey, Object... args);

    void confirmationRequestPrintLn();

    boolean isOngoing();
}
