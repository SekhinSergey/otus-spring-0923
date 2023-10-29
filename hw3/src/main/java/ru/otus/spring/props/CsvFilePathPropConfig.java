package ru.otus.spring.props;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Configuration
@ConfigurationProperties(prefix = "test")
public class CsvFilePathPropConfig implements PropConfig {

    private String csvResourcePath;

    public String getProperty() {
        return csvResourcePath;
    }
}
