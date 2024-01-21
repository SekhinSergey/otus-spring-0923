package ru.otus.spring.repository;

import ru.otus.spring.model.Author;
import ru.otus.spring.model.Book;
import ru.otus.spring.model.Genre;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

public class TestBookUtils {

    private static final String SIZE_ASSERTION_RULE = "java:S5838";

    public static List<Author> getDbAuthors() {
        return LongStream.range(1, 4)
                .boxed()
                .map(id -> new Author(id.toString(), "Author_" + id))
                .toList();
    }

    public static List<Genre> getDbGenres() {
        return LongStream.range(1, 7)
                .boxed()
                .map(id -> new Genre(id.toString(), "Genre_" + id))
                .toList();
    }

    public static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4)
                .boxed()
                .map(id -> new Book(
                        id.toString(),
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
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
    public static void assertThatActualAndExpectedGenreListAreEqual(List<Genre> actualGenreList,
                                                                    List<Genre> expectedGenreList) {
        assertThat(actualGenreList.size()).isEqualTo(expectedGenreList.size());
        var expectedGenreMap = expectedGenreList.stream()
                .collect(Collectors.toMap(Genre::getId, Function.identity()));
        actualGenreList.forEach(actualGenre -> {
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
}
