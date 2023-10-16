package ru.otus.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.spring.props.AppProps;

@Configuration
@PropertySource("/csv-test.properties")
public class GoodCsvProps implements AppProps {

    @Value("${question-good.source}")
    private String goodCsvResourcePath;

    public String getProperty() {
        return goodCsvResourcePath;
    }
}
