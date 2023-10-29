package ru.otus.spring.io;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.otus.spring.props.PropConfig;

import java.util.Locale;

@Component
public class ResourceBundleReaderImpl implements ResourceBundleReader {

    private static final String LOCALE_PROP_CONFIG_BEAN_NAME = "localePropConfig";

    private final MessageSource messageSource;

    private final PropConfig localePropConfig;

    public ResourceBundleReaderImpl(MessageSource messageSource,
                                    @Qualifier(LOCALE_PROP_CONFIG_BEAN_NAME) PropConfig localePropConfig) {
        this.messageSource = messageSource;
        this.localePropConfig = localePropConfig;
    }

    public String getMessage(String propertyKey) {
        return messageSource.getMessage(
                propertyKey,
                new Object[]{StringUtils.EMPTY},
                (Locale) localePropConfig.getProperty());
    }

    public String getMessageWithArgs(String propertyKey, Object ...args) {
        return messageSource.getMessage(propertyKey, args, (Locale) localePropConfig.getProperty());
    }
}
