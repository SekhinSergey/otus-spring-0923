package ru.otus.spring.out;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class ConsolePrinter implements Printer {

    public void print(Object o) {
        System.out.println(o);
    }

    public void skipLine() {
        System.out.println(StringUtils.EMPTY);
    }
}
