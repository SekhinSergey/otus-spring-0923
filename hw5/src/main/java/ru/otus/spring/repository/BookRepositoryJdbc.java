package ru.otus.spring.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
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

    private static final String GENRE_ID = "genre_id";

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    private final GenreRepository genreRepository;

    @Override
    public Optional<Book> findByTitle(String title) {
        Map<String, Object> params = Collections.singletonMap(TITLE, title);
        return namedParameterJdbcOperations.query(
                "select " +
                        "b.id, " +
                        "b.title, " +
                        "b.author_id, " +
                        "a.full_name, " +
                        "bg.genre_id, " +
                        "(select g.name from genres g where g.id = bg.genre_id) as genre_name " +
                        "from books b " +
                            "join authors a on a.id = b.author_id " +
                            "join books_genres bg on bg.book_id = b.id " +
                        "where b.title = :title",
                params,
                new BookResultSetExtractor());
    }

    @Override
    public Optional<Book> findById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return namedParameterJdbcOperations.query(
                "select " +
                        "b.id, " +
                        "b.title, " +
                        "b.author_id, " +
                        "a.full_name, " +
                        "bg.genre_id, " +
                        "(select g.name from genres g where g.id = bg.genre_id) as genre_name " +
                        "from books b " +
                            "join authors a on a.id = b.author_id " +
                            "join books_genres bg on bg.book_id = b.id " +
                        "where b.id = :id",
                params,
                new BookResultSetExtractor());
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

    @Override
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
                new BooksResultSetExtractor());
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

    @Override
    public Book save(Book book) {
        return book.getId() == null ? insert(book) : update(book);
    }

    @Override
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
        List<Map<String, Long>> batchMapList = new ArrayList<>();
        book.getGenres().forEach(genre -> {
            Map<String, Long> batchMap = new HashMap<>();
            batchMap.put(BOOK_ID, book.getId());
            batchMap.put(GENRE_ID, genre.getId());
            batchMapList.add(batchMap);
        });
        namedParameterJdbcOperations.batchUpdate(
                "insert into books_genres (book_id, genre_id) values (:book_id, :genre_id)",
                SqlParameterSourceUtils.createBatch(batchMapList));
    }

    private void removeGenresRelationsFor(long bookId) {
        Map<String, Object> params = Collections.singletonMap(BOOK_ID, bookId);
        namedParameterJdbcOperations.update("delete from books_genres where book_id = :book_id", params);
    }

    private static class BookResultSetExtractor implements ResultSetExtractor<Optional<Book>> {

        public Optional<Book> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Book book = new Book();
            while (rs.next()) {
                long id = rs.getLong("id");
                String title = rs.getString(TITLE);
                long authorId = rs.getInt(AUTHOR_ID);
                String fullName = rs.getString("full_name");
                Author author = new Author(authorId, fullName);
                long genreId = rs.getLong(GENRE_ID);
                String genreName = rs.getString("genre_name");
                Genre genre = new Genre(genreId, genreName);
                if (isNull(book.getId())) {
                    book.setId(id);
                    book.setTitle(title);
                    book.setAuthor(author);
                    book.setGenres(new ArrayList<>());
                }
                book.getGenres().add(genre);
            }
            return isNull(book.getId()) ? Optional.empty() : Optional.of(book);
        }
    }

    private static class BooksResultSetExtractor implements ResultSetExtractor<List<Book>> {

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
            long genreId = rs.getInt(GENRE_ID);
            return new BookGenreRelation(bookId, genreId);
        }
    }

    @Override
    public int countByAuthorId(long authorId) {
        return findAll().stream()
                .filter(book -> authorId == book.getAuthor().getId())
                .toList()
                .size();
    }

    @Override
    public int countByAuthorFullName(String authorFullName) {
        return findAll().stream()
                .filter(book -> authorFullName.equals(book.getAuthor().getFullName()))
                .toList()
                .size();
    }

    @Override
    public int countByGenreId(long genreId) {
        return findAll().stream()
                .mapToInt(book -> (int) book.getGenres().stream()
                        .filter(genre -> genreId == genre.getId())
                        .count())
                .sum();
    }

    @Override
    public int countByGenreName(String genreName) {
        return findAll().stream()
                .mapToInt(book -> (int) book.getGenres().stream()
                        .filter(genre -> genreName.equals(genre.getName()))
                        .count())
                .sum();
    }
}
