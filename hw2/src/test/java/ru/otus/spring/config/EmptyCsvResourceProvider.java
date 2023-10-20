package ru.otus.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.spring.props.ResourceProvider;

@Configuration
@PropertySource("/csv-test.properties")
public class EmptyCsvResourceProvider implements ResourceProvider {

    @Value("${question-empty.source}")
    private String emptyCsvResourcePath;

    public String getResourceName() {
        return emptyCsvResourcePath;
    }
}
