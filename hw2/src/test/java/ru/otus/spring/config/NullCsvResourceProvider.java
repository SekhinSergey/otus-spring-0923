package ru.otus.spring.config;

import org.springframework.context.annotation.Configuration;
import ru.otus.spring.props.ResourceProvider;

@Configuration
public class NullCsvResourceProvider implements ResourceProvider {

    public String getResourceName() {
        return null;
    }
}
