package ru.otus.spring.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.props.ResourceProvider;

@Configuration
public class EmptyCsvPathResourceProvider implements ResourceProvider {

    public String getResourceName() {
        return StringUtils.EMPTY;
    }
}
