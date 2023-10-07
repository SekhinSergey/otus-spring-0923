package ru.otus.spring.out;

public class ConsolePrinter implements Printer {

    public void print(Object o) {
        System.out.println(o);
    }
}
