package ru.otus.spring.utils;

import ru.otus.spring.model.Author;
import ru.otus.spring.model.Book;
import ru.otus.spring.model.Comment;
import ru.otus.spring.model.Genre;

import java.util.Set;

public class EntityUtils {

    public static final String FIRST_ID = "1";

    public static final String SECOND_ID = "2";

    public static final String THIRD_ID = "3";

    public static final String FOURTH_ID = "4";

    public static final String FIFTH_ID = "5";

    public static final String SIXTH_ID = "6";

    public static final String FIRST_BOOK_TITLE = "BookTitle_1";

    public static final String SECOND_BOOK_TITLE = "BookTitle_2";

    public static final String THIRD_BOOK_TITLE = "BookTitle_3";

    public static final String FIRST_AUTHOR_NAME = "Author_1";

    public static final String SECOND_AUTHOR_NAME = "Author_2";

    public static final String THIRD_AUTHOR_NAME = "Author_3";

    public static final String FIRST_GENRE_NAME = "Genre_1";

    public static final String SECOND_GENRE_NAME = "Genre_2";

    public static final String THIRD_GENRE_NAME = "Genre_3";

    public static final String FOURTH_GENRE_NAME = "Genre_4";

    public static final String FIFTH_GENRE_NAME = "Genre_5";

    public static final String SIXTH_GENRE_NAME = "Genre_6";

    public static final String FIRST_COMMENT = "Comment_1";

    public static final String SECOND_COMMENT = "Comment_2";

    public static final String THIRD_COMMENT = "Comment_3";

    private EntityUtils () {
    }

    public static Author getFirstAuthor() {
        return new Author(FIRST_ID, FIRST_AUTHOR_NAME);
    }

    public static Author getSecondAuthor() {
        return new Author(SECOND_ID, SECOND_AUTHOR_NAME);
    }

    public static Author getThirdAuthor() {
        return new Author(THIRD_ID, THIRD_AUTHOR_NAME);
    }

    public static Genre getFirstGenre() {
        return new Genre(FIRST_ID, FIRST_GENRE_NAME);
    }

    public static Genre getSecondGenre() {
        return new Genre(SECOND_ID, SECOND_GENRE_NAME);
    }

    public static Genre getThirdGenre() {
        return new Genre(THIRD_ID, THIRD_GENRE_NAME);
    }

    public static Genre getFourthGenre() {
        return new Genre(FOURTH_ID, FOURTH_GENRE_NAME);
    }

    public static Genre getFifthGenre() {
        return new Genre(FIFTH_ID, FIFTH_GENRE_NAME);
    }

    public static Genre getSixthGenre() {
        return new Genre(SIXTH_ID, SIXTH_GENRE_NAME);
    }

    public static Book getFirstBook() {
        return new Book(FIRST_ID, FIRST_BOOK_TITLE, getFirstAuthor(), Set.of(getFirstGenre(), getSecondGenre()));
    }

    public static Book getSecondBook() {
        return new Book(SECOND_ID, SECOND_BOOK_TITLE, getSecondAuthor(), Set.of(getThirdGenre(), getFourthGenre()));
    }

    public static Book getThirdBook() {
        return new Book(THIRD_ID, THIRD_BOOK_TITLE, getThirdAuthor(), Set.of(getFifthGenre(), getSixthGenre()));
    }

    public static Comment getFirstComment() {
        return new Comment(FIRST_ID, FIRST_COMMENT, getFirstBook());
    }

    public static Comment getSecondComment() {
        return new Comment(SECOND_ID, SECOND_COMMENT, getSecondBook());
    }

    public static Comment getThirdComment() {
        return new Comment(THIRD_ID, THIRD_COMMENT, getThirdBook());
    }
}
