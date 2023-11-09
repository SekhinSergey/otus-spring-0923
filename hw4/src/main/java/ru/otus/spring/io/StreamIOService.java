package ru.otus.spring.io;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

@Component
public class StreamIOService implements IOService {

    private final PrintStream printStream;

    private final Scanner scanner;

    public StreamIOService(@Value("#{T(System).out}") PrintStream printStream,
                           @Value("#{T(System).in}") InputStream inputStream) {
        this.printStream = printStream;
        this.scanner = new Scanner(inputStream);
    }

    public void printLn(Object o) {
        printStream.println(o);
    }

    public void skipPrintLn() {
        printStream.println(StringUtils.EMPTY);
    }

    public String readLine() {
        return scanner.nextLine();
    }
}
