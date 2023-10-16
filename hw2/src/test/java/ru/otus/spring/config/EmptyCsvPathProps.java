package ru.otus.spring.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.props.AppProps;

@Configuration
public class EmptyCsvPathProps implements AppProps {

    public String getProperty() {
        return StringUtils.EMPTY;
    }
}
