package ru.otus.spring.utils;

import org.apache.commons.io.IOUtils;
import ru.otus.spring.model.Author;
import ru.otus.spring.model.Book;
import ru.otus.spring.model.Comment;
import ru.otus.spring.model.Genre;
import ru.otus.spring.model.User;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.assertThat;

public class Utils {

    public static final int FIRST_INDEX = 0;

    public static final long NOT_EXISTENT_ID = 7;

    public static final String NO_AUTHOR_BY_SPECIFIC_ID_ERROR_MESSAGE = "Author with id 7 not found";

    public static final String NO_BOOK_BY_SPECIFIC_ID_ERROR_MESSAGE = "Book with id 7 not found";

    private static final String SIZE_ASSERTION_RULE = "java:S5838";

    public static List<Author> getDbAuthors() {
        return LongStream.range(1, 4)
                .boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    public static List<Genre> getDbGenres() {
        return LongStream.range(1, 7)
                .boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    public static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4)
                .boxed()
                .map(id -> new Book(
                        (long) id,
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        new HashSet<>(dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2))
                ))
                .toList();
    }

    public static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }

    public static void assertThatActualAndExpectedAuthorAreEqual(Author actualAuthor, Author expectedAuthor) {
        assertThat(actualAuthor.getId()).isEqualTo(expectedAuthor.getId());
        assertThat(actualAuthor.getFullName()).isEqualTo(expectedAuthor.getFullName());
    }

    public static void assertThatActualAndExpectedGenreAreEqual(Genre actualGenre, Genre expectedGenre) {
        assertThat(actualGenre.getId()).isEqualTo(expectedGenre.getId());
        assertThat(actualGenre.getName()).isEqualTo(expectedGenre.getName());
    }

    @SuppressWarnings(SIZE_ASSERTION_RULE)
    public static void assertThatActualAndExpectedGenreListAreEqual(Set<Genre> actualGenreSet,
                                                                    Set<Genre> expectedGenreSet) {
        assertThat(actualGenreSet.size()).isEqualTo(expectedGenreSet.size());
        var expectedGenreMap = expectedGenreSet.stream()
                .collect(toMap(Genre::getId, Function.identity()));
        actualGenreSet.forEach(actualGenre -> {
            var expectedGenre = expectedGenreMap.get(actualGenre.getId());
            assertThatActualAndExpectedGenreAreEqual(actualGenre, expectedGenre);
        });
    }

    public static void assertThatActualAndExpectedBookAreEqual(Book actualBook, Book expectedBook) {
        assertThat(actualBook.getId()).isEqualTo(expectedBook.getId());
        assertThat(actualBook.getTitle()).isEqualTo(expectedBook.getTitle());
        assertThatActualAndExpectedAuthorAreEqual(actualBook.getAuthor(), expectedBook.getAuthor());
        assertThatActualAndExpectedGenreListAreEqual(actualBook.getGenres(), expectedBook.getGenres());
    }

    public static void assertThatActualAndExpectedUserAreEqual(User actualUser, User expectedUser) {
        assertThat(actualUser.getId()).isEqualTo(expectedUser.getId());
        assertThat(actualUser.getEmail()).isEqualTo(expectedUser.getEmail());
        assertThat(actualUser.getRoles()).isEqualTo(expectedUser.getRoles());
        assertThat(actualUser.getPassword()).isEqualTo(expectedUser.getPassword());
    }

    public static List<User> getDbUsers() {
        return List.of(
                new User(1L, "mail1@mail.com", "ROLE_1", "Password_1"),
                new User(2L, "mail2@mail.com", "ROLE_2", "Password_2"),
                new User(3L, "mail3@mail.com", "ROLE_3", "Password_3"));
    }

    public static List<Comment> getDbComments() {
        return IntStream.range(1, 4)
                .boxed()
                .map(id ->
                        new Comment((long) id, ("Comment_" + id), getDbBooks().get(id - 1), getDbUsers().get(id - 1)))
                .toList();
    }

    public static String getStringJsonByFilePath(String filePath) throws IOException {
        return IOUtils.toString(new FileReader(filePath));
    }
}
