package ru.otus.spring.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.exception.EntityNotFoundException;
import ru.otus.spring.model.Author;
import ru.otus.spring.model.Book;
import ru.otus.spring.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static ru.otus.spring.model.Genre.buildGenreMap;
import static ru.otus.spring.repository.BookRepositoryJdbc.BookGenreRelation.buildRelationMap;

@Repository
@RequiredArgsConstructor
public class BookRepositoryJdbc implements BookRepository {

    private static final String BOOK_ID = "book_id";

    private static final String TITLE = "title";

    private static final String AUTHOR_ID = "author_id";

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    public Optional<Book> findByTitle(String title) {
        Optional<Book> book = getBooksByTitleWithoutGenresAndAuthorFullName(title);
        return fillAndReturn(book);
    }

    private Optional<Book> getBooksByTitleWithoutGenresAndAuthorFullName(String title) {
        Map<String, Object> params = Collections.singletonMap(TITLE, title);
        Optional<Book> book = Optional.ofNullable(namedParameterJdbcOperations.queryForObject(
                "select id, title, author_id from books where title = :title",
                params,
                new BookRowMapper()));
        if (book.isEmpty()) {
            throw new EntityNotFoundException("Book with title %s not found".formatted(title));
        }
        return book;
    }

    public Optional<Book> findById(long id) {
        Optional<Book> book = getBooksByIdWithoutGenresAndAuthorFullName(id);
        if (book.isEmpty()) {
            return book;
        }
        return fillAndReturn(book);
    }

    @SuppressWarnings("all")
    private Optional<Book> fillAndReturn(Optional<Book> book) {
        var author = getAuthorById(book.get().getAuthor().getId());
        var relations = getGenreRelationsByBookId(book.get().getId());
        var genres = genreRepository.findAll();
        mergeBooksInfoById(book, author, relations, genres);
        return book;
    }

    private Optional<Book> getBooksByIdWithoutGenresAndAuthorFullName(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        Optional<Book> book;
        try {
            book = Optional.ofNullable(namedParameterJdbcOperations.queryForObject(
                    "select id, title, author_id from books where id = :id",
                    params,
                    new BookRowMapper()));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
        if (book.isEmpty()) {
            throw new EntityNotFoundException("Book with id %d not found".formatted(id));
        }
        return book;
    }

    private Author getAuthorById(long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(id)));
    }

    private List<BookGenreRelation> getGenreRelationsByBookId(long bookId) {
        Map<String, Object> params = Collections.singletonMap(BOOK_ID, bookId);
        return namedParameterJdbcOperations.query(
                "select book_id, genre_id from books_genres where book_id = :book_id",
                params,
                new BookGenreRelationRowMapper());
    }

    @SuppressWarnings("all")
    private void mergeBooksInfoById(Optional<Book> book,
                                    Author author,
                                    List<BookGenreRelation> relations,
                                    List<Genre> genres) {
        String fullName = author.getFullName();
        book.get().getAuthor().setFullName(fullName);
        Set<Long> genreIdSet = relations.stream()
                .mapToLong(BookGenreRelation::genreId)
                .boxed()
                .collect(Collectors.toSet());
        Map<Long, Genre> genreMap = buildGenreMap(genres);
        genreIdSet.stream()
                .map(genreMap::get)
                .forEach(genre -> book.get().getGenres().add(genre));

    }

    @SuppressWarnings("null")
    public List<Book> findAll() {
        var books = getAllBooksWithoutGenres();
        var relations = getAllGenreRelations();
        var genres = genreRepository.findAll();
        mergeBooksInfo(books, relations, genres);
        return books;
    }

    private List<Book> getAllBooksWithoutGenres() {
        return namedParameterJdbcOperations.query(
                "select " +
                        "b.id as id, " +
                        "b.title as title, " +
                        "b.author_id as author_id, " +
                        "a.full_name as full_name " +
                    "from books b " +
                        "join authors a on author_id = a.id ",
                new BookResultSetExtractor());
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return namedParameterJdbcOperations.query(
                "select book_id, genre_id from books_genres",
                new BookGenreRelationRowMapper());
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres,
                                List<BookGenreRelation> relations,
                                List<Genre> genres) {
        Map<Long, Set<Long>> relationMap = buildRelationMap(relations);
        Map<Long, Genre> genreMap = buildGenreMap(genres);
        booksWithoutGenres.forEach(book -> {
            long bookId = book.getId();
            Set<Long> genreIds = relationMap.get(bookId);
            List<Genre> bookGenres = genreIds.stream()
                    .map(genreMap::get)
                    .toList();
            book.setGenres(bookGenres);
        });
    }

    public Book save(Book book) {
        return book.getId() == 0 ? insert(book) : update(book);
    }

    public void deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        namedParameterJdbcOperations.update("delete from books where id = :id", params);
        removeGenresRelationsFor(id);
    }

    private Book insert(Book book) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(TITLE, book.getTitle());
        params.addValue(AUTHOR_ID, book.getAuthor().getId());
        var keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcOperations.update(
                "insert into books (title, author_id) values (:title, :author_id)",
                params,
                keyHolder,
                new String[]{"id"});
        Long id = keyHolder.getKeyAs(Long.class);
        if (isNull(id)) {
            throw new EntityNotFoundException("Book id is null in new book");
        }
        book.setId(id);
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(TITLE, book.getTitle());
        params.addValue(AUTHOR_ID, book.getAuthor().getId());
        long bookId = book.getId();
        params.addValue("id", bookId);
        namedParameterJdbcOperations.update(
                "update books set title = :title, author_id = :author_id where id = :id",
                params);
        removeGenresRelationsFor(book.getId());
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        book.getGenres().forEach(genre -> {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue(BOOK_ID, book.getId());
            params.addValue("genre_id", genre.getId());
            namedParameterJdbcOperations.update(
                    "insert into books_genres (book_id, genre_id) values (:book_id, :genre_id)",
                    params);
        });
    }

    private void removeGenresRelationsFor(long bookId) {
        Map<String, Object> params = Collections.singletonMap(BOOK_ID, bookId);
        namedParameterJdbcOperations.update("delete from books_genres where book_id = :book_id", params);
    }

    private static class BookRowMapper implements RowMapper<Book> {

        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String title = rs.getString(TITLE);
            long authorId = rs.getInt(AUTHOR_ID);
            return new Book(id, title, new Author(authorId, null), new ArrayList<>());
        }
    }

    private static class BookResultSetExtractor implements ResultSetExtractor<List<Book>> {

        public List<Book> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<Book> books = new ArrayList<>();
            while (rs.next()) {
                long id = rs.getLong("id");
                String title = rs.getString(TITLE);
                long authorId = rs.getInt(AUTHOR_ID);
                String fullName = rs.getString("full_name");
                Book book = new Book(id, title, new Author(authorId, fullName), new ArrayList<>());
                books.add(book);
            }
            return books;
        }
    }

    record BookGenreRelation(long bookId, long genreId) {

        static Map<Long, Set<Long>> buildRelationMap(List<BookGenreRelation> relations) {
            Map<Long, Set<Long>> relationMap = new HashMap<>();
            relations.forEach(relation -> {
                long bookId = relation.bookId();
                if (relationMap.containsKey(bookId)) {
                    relationMap.get(bookId).add(relation.genreId());
                } else {
                    relationMap.put(bookId, new HashSet<>(List.of(relation.genreId())));
                }
            });
            return relationMap;
        }
    }

    private static class BookGenreRelationRowMapper implements RowMapper<BookGenreRelation> {

        public BookGenreRelation mapRow(ResultSet rs, int rowNumber) throws SQLException {
            long bookId = rs.getInt(BOOK_ID);
            long genreId = rs.getInt("genre_id");
            return new BookGenreRelation(bookId, genreId);
        }
    }

    public int countByAuthorId(long authorId) {
        return findAll().stream()
                .filter(book -> authorId == book.getAuthor().getId())
                .toList()
                .size();
    }

    public int countByAuthorFullName(String authorFullName) {
        return findAll().stream()
                .filter(book -> authorFullName.equals(book.getAuthor().getFullName()))
                .toList()
                .size();
    }

    public int countByGenreId(long genreId) {
        return findAll().stream()
                .mapToInt(book -> (int) book.getGenres().stream()
                        .filter(genre -> genreId == genre.getId())
                        .count())
                .sum();
    }

    public int countByGenreName(String genreName) {
        return findAll().stream()
                .mapToInt(book -> (int) book.getGenres().stream()
                        .filter(genre -> genreName.equals(genre.getName()))
                        .count())
                .sum();
    }
}
