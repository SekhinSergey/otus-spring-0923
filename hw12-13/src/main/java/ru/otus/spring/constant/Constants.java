package ru.otus.spring.constant;

public class Constants {

    public static final String NO_BOOK_TITLE_ERROR_MESSAGE = "Book title value should not be blank";

    public static final String NO_AUTHOR_ID_ERROR_MESSAGE = "Author ID value should not be null";

    public static final String NO_GENRE_IDS_ERROR_MESSAGE = "Set of genre IDs value should not be empty";

    public static final String NO_GENRE_ID_ERROR_MESSAGE = "Genre ID value should not be null";

    public static final String NO_COMMENT_TEXT_ERROR_MESSAGE = "Comment text value should not be blank";

    public static final String NO_BOOK_ID_ERROR_MESSAGE = "Book ID value should not be null";

    public static final String NO_USER_ID_ERROR_MESSAGE = "User ID value should not be null";

    public static final String NO_AUTHOR_BY_ID_ERROR_MESSAGE = "Author with id %d not found";

    public static final String NO_BOOK_BY_ID_ERROR_MESSAGE = "Book with id %d not found";

    public static final String GENRES_SIZE_ERROR_MESSAGE
            = "The number of requested genres does not match the number in the database";

    public static final String EMAIL_FORMAT_ERROR_MESSAGE = "Invalid email format";

    public static final String NO_EMAIL_ERROR_MESSAGE = "User email must not be empty";

    public static final String NO_PASSWORD_ERROR_MESSAGE = "User password must not be empty";

    public static final String NO_USER_BY_EMAIL_ERROR_MESSAGE = "User with email %s does not exist";

    public static final String NO_USER_BY_ID_ERROR_MESSAGE = "User with ID %d does not exist";

    public static final int FIFTEEN_MINUTES_IN_SECONDS = 900;

    public static final int FIFTEEN_DAYS_IN_SECONDS = 1296000;

    private Constants() {
    }
}
