package ru.otus.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.spring.props.AppProps;

@Configuration
@PropertySource("/csv-test.properties")
public class EmptyCsvProps implements AppProps {

    @Value("${question-empty.source}")
    private String emptyCsvResourcePath;

    public String getProperty() {
        return emptyCsvResourcePath;
    }
}
