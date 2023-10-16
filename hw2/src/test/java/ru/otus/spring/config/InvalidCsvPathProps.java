package ru.otus.spring.config;

import org.springframework.context.annotation.Configuration;
import ru.otus.spring.props.AppProps;

@Configuration
public class InvalidCsvPathProps implements AppProps {

    public String getProperty() {
        return "1.csv";
    }
}
