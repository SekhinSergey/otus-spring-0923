package ru.otus.spring.utils;

public class Messages {

    public static final String NO_BOOK_TITLE_ERROR_MESSAGE = "Book title value should not be blank";

    public static final String NO_AUTHOR_ID_ERROR_MESSAGE = "Author ID value should not be blank";

    public static final String NO_GENRE_IDS_ERROR_MESSAGE = "Set of genre IDs value should not be empty";

    public static final String NO_GENRE_ID_ERROR_MESSAGE = "Genre ID value should not be blank";

    public static final String NO_COMMENT_TEXT_ERROR_MESSAGE = "Comment text value should not be blank";

    public static final String NO_BOOK_ID_ERROR_MESSAGE = "Book ID value should not be blank";

    public static final String NO_AUTHOR_BY_ID_ERROR_MESSAGE = "Author with id %s not found";

    public static final String NO_BOOK_BY_ID_ERROR_MESSAGE = "Book with id %s not found";

    public static final String NO_COMMENT_BY_ID_ERROR_MESSAGE = "Comment with id %s not found";

    public static final String GENRES_SIZE_ERROR_MESSAGE
            = "The number of requested genres does not match the number in the database";

    private Messages() {
    }
}
