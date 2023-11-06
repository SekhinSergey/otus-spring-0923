package ru.otus.spring.utils;

public class TestConstants {

    public static final String NULL_CSV_BASE_MESSAGE = "CSV parsing stopped with an error: ";

    public static final String NULL_CSV_PATH_MESSAGE = NULL_CSV_BASE_MESSAGE + "Path must not be null";

    public static final String INVALID_CSV_PATH = "1.csv";

    public static final String EMPTY_CSV_PATH = "empty.csv";

    public static final String INCORRECT_CSV_PATH = "incorrect.csv";

    public static final String COLUMN_INDEX_OUT_OF_BOUNDS_ERROR_MESSAGE = "Column with index 1 not found in CSV";

    public static final String GOOD_CSV_PATH = "good.csv";
}
