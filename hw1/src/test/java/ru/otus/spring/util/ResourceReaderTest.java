package ru.otus.spring.util;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.spring.exception.CsvReadException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResourceReaderTest {

    private ResourceReader resourceReader;

    @BeforeEach
    void setUp() {
        resourceReader = new ClassPathXmlApplicationContext("test-spring-context.xml")
                .getBean(ResourceReader.class);
    }

    @Test
    void assertNotNullResourceReader() {
        assertNotNull(resourceReader);
    }

    @Test
    void assertThrowsCsvReadExceptionByNull() {
        assertEquals("CSV parsing stopped with an error: Path must not be null",
                assertThrows(CsvReadException.class, () -> {
                    resourceReader.readResource(null);
                }).getMessage());
    }

    @Test
    void assertReturnEmptyListByEmptyPath() {
        assertDoesNotThrowAndReturnResult(StringUtils.EMPTY, Collections.emptyList());
    }

    @Test
    void assertThrowsCsvReadExceptionByBadPath() {
        String fileName = "1.csv";
        assertEquals("CSV parsing stopped with an error: class path resource [" +
                        fileName + "] cannot be opened because it does not exist",
                assertThrows(CsvReadException.class, () -> {
                    resourceReader.readResource(fileName);
                }).getMessage());
    }

    @Test
    void assertReturnEmptyListByEmptyCsv() {
        assertDoesNotThrowAndReturnResult("empty.csv", Collections.emptyList());
    }

    @Test
    void assertReturnSingletonByNoDelimiterCsv() {
        assertDoesNotThrowAndReturnResult("no-delimiter.csv",
                Collections.singletonList(Collections.singletonList("1")));
    }

    @Test
    void assertReturnListByGoodCsv() {
        assertDoesNotThrowAndReturnResult("good.csv",
                Collections.singletonList(Arrays.asList("1", "2")));
    }

    @SneakyThrows
    private void assertDoesNotThrowAndReturnResult(String fileName, List<List<String>> expected) {
        assertDoesNotThrow(() -> {
            resourceReader.readResource(fileName);
        });
        assertEquals(expected, resourceReader.readResource(fileName));
    }
}