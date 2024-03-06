package ru.otus.spring.constant;

public class Constants {

    public static final String NO_AUTHOR_BY_ID_ERROR_MESSAGE = "Author with id %d not found";

    public static final String NO_BOOK_BY_ID_ERROR_MESSAGE = "Book with id %d not found";

    public static final String BOOKS_SIZE_ERROR_MESSAGE
            = "The number of requested books does not match the number in the database";

    public static final String AUTHORS_SIZE_ERROR_MESSAGE
            = "The number of requested authors does not match the number in the database";

    public static final String GENRES_SIZE_ERROR_MESSAGE
            = "The number of requested genres does not match the number in the database";

    private Constants() {

    }
}
