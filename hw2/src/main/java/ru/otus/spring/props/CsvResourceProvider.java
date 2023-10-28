package ru.otus.spring.props;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:csv.properties")
public class CsvResourceProvider implements ResourceProvider {

    @Value("${question.source}")
    private String csvResourcePath;

    public String getResourceName() {
        return csvResourcePath;
    }
}
