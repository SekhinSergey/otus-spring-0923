package ru.otus.spring.props;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:csv.properties")
public class CsvProps implements AppProps {

    @Value("${question.source}")
    private String csvResourcePath;

    @Override
    public String getProperty() {
        return csvResourcePath;
    }
}
