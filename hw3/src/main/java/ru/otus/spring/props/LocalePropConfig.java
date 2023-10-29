package ru.otus.spring.props;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Setter
@Configuration
@ConfigurationProperties(prefix = "lang")
public class LocalePropConfig implements PropConfig {

    private Locale locale;

    public Locale getProperty() {
        return locale;
    }
}
