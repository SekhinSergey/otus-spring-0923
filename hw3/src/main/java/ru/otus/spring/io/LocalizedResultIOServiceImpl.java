package ru.otus.spring.io;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocalizedResultIOServiceImpl implements LocalizedResultIOService {

    private static final String MIN_RESULT = "LocalizedResultIOService.min.result";

    private static final String FAILURE_MESSAGE = "LocalizedResultIOService.failure.message";

    private static final String CONGRATULATORY_MESSAGE = "LocalizedResultIOService.congratulatory.message";

    private final ResourceBundleReader resourceBundleReader;

    private final LocalizedIOService localizedIOService;

    public int getMinResult() {
        String message = resourceBundleReader.getMessage(MIN_RESULT);
        return Integer.parseInt(message);
    }

    public void failureMessagePrintLn() {
        localizedIOService.localizedPrintLn(FAILURE_MESSAGE);
    }

    public void congratulatoryMessagePrintLn() {
        localizedIOService.localizedPrintLn(CONGRATULATORY_MESSAGE);
    }
}
