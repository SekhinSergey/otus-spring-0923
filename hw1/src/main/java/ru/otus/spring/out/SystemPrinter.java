package ru.otus.spring.out;

public class SystemPrinter implements Printer {

    public void print(Object o) {
        System.out.println(o);
    }
}
