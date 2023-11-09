package ru.otus.spring.io;

public interface ResourceBundleReader {

    String getMessage(String propertyKey);

    String getMessageWithArgs(String propertyKey, Object ...args);
}
