package ru.otus.spring.out;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ConsoleIOService implements IOService {

    public void printLn(Object o) {
        System.out.println(o);
    }

    public void skipPrintLn() {
        System.out.println(StringUtils.EMPTY);
    }

    public String readLine() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
