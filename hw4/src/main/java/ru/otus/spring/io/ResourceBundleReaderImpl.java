package ru.otus.spring.io;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.otus.spring.props.LocaleConfig;

@Component
@RequiredArgsConstructor
public class ResourceBundleReaderImpl implements ResourceBundleReader {

    private final MessageSource messageSource;

    private final LocaleConfig localeConfig;

    public String getMessage(String propertyKey) {
        return messageSource.getMessage(
                propertyKey,
                new Object[]{StringUtils.EMPTY},
                localeConfig.getLocale());
    }

    public String getMessageWithArgs(String propertyKey, Object ...args) {
        return messageSource.getMessage(propertyKey, args, localeConfig.getLocale());
    }
}
