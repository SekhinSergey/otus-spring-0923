package ru.otus.spring.util;

import org.springframework.core.io.ClassPathResource;
import ru.otus.spring.exception.CsvReadException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvResourceReader implements ResourceReader {

    public List<List<String>> readResource(String fileName) throws CsvReadException {
        List<List<String>> csvList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new ClassPathResource(fileName).getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                csvList.add(Arrays.asList(values));
            }
        } catch (Exception exception) {
            throw new CsvReadException("CSV parsing stopped with an error: " + exception.getMessage());
        }
        return csvList;
    }
}