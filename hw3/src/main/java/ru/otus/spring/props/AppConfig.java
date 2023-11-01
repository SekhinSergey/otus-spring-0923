package ru.otus.spring.props;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Setter
@Configuration
@ConfigurationProperties(prefix = "test")
public class AppConfig implements TestFileNameProvider, LocaleConfig, TestConfig {

    private String csvResourcePath;

    private Locale locale;

    private int minResult;

    public String getTestFileName() {
        return csvResourcePath;
    }

    public Locale getLocale() {
        return locale;
    }

    public int getMinResult() {
        return minResult;
    }
}
