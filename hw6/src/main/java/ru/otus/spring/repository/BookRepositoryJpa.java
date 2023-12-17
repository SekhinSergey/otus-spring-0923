package ru.otus.spring.repository;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import ru.otus.spring.exception.EntityNotFoundException;
import ru.otus.spring.model.Author;
import ru.otus.spring.model.Book;
import ru.otus.spring.model.Genre;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
@RequiredArgsConstructor
public class BookRepositoryJpa implements BookRepository {

    private static final String BOOK_ENTITY_GRAPH = "book-entity-graph";

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<Book> findByTitle(String title) {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph(BOOK_ENTITY_GRAPH);
        TypedQuery<Book> query = entityManager.createQuery(
                "select b from Book b where b.title = :title", Book.class);
        query.setParameter("title", title);
        query.setHint(FETCH.getKey(), entityGraph);
        return getOptionalBook(query);
    }

    @Override
    public Optional<Book> findById(long id) {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph(BOOK_ENTITY_GRAPH);
        TypedQuery<Book> query = entityManager.createQuery(
                "select b from Book b where b.id = :id", Book.class);
        query.setParameter("id", id);
        query.setHint(FETCH.getKey(), entityGraph);
        return getOptionalBook(query);
    }

    private static Optional<Book> getOptionalBook(TypedQuery<Book> query) {
        Book book;
        try {
            book = query.getSingleResult();
        } catch (NoResultException exception) {
            book = null;
        }
        return isNull(book) ? Optional.empty() : Optional.of(book);
    }

    @Override
    public List<Book> findAll() {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph(BOOK_ENTITY_GRAPH);
        TypedQuery<Book> query = entityManager.createQuery("select b from Book b", Book.class);
        query.setHint(FETCH.getKey(), entityGraph);
        return query.getResultList();
    }

    @Override
    public Optional<Book> save(Book book) {
        Long id = book.getId();
        return id == null || findById(id).isEmpty() ? insert(book) : update(book);
    }

    private Optional<Book> insert(Book book) {
        if (book.getId() == null) {
            entityManager.persist(book);
            return findByTitle(book.getTitle());
        }
        Book returnedBook = entityManager.merge(book);
        return isNull(returnedBook) ? Optional.empty() : Optional.of(returnedBook);
    }

    private Optional<Book> update(Book book) {
        long id = book.getId();
        updateBook(book, id);
        deleteBooksGenres(id);
        insertBooksGenres(book, id);
        return findById(id);
    }

    private void updateBook(Book book, long id) {
        String title = book.getTitle();
        Author author = book.getAuthor();
        Query bookQuery = entityManager.createQuery(
                "update Book b set b.title = :title, b.author = :author where b.id = :id");
        bookQuery.setParameter("id", id);
        bookQuery.setParameter("title", title);
        bookQuery.setParameter("author", author);
        bookQuery.executeUpdate();
        entityManager.clear();
    }

    private void deleteBooksGenres(long id) {
        Query deleteBgQuery = entityManager.createNativeQuery("delete from books_genres bg where bg.book_id = :id");
        deleteBgQuery.setParameter("id", id);
        deleteBgQuery.executeUpdate();
    }

    private void insertBooksGenres(Book book, long id) {
        StringBuilder values = new StringBuilder();
        for (Iterator<Genre> iterator = book.getGenres().iterator(); iterator.hasNext(); ) {
            values
                    .append("(")
                    .append(id)
                    .append(",")
                    .append(StringUtils.SPACE)
                    .append(iterator.next().getId())
                    .append(")");
            if (iterator.hasNext()) {
                values
                        .append(",")
                        .append(StringUtils.SPACE);
            }
        }
        Query insertBgQuery = entityManager.createNativeQuery(
                "insert into books_genres(book_id, genre_id) values " + values);
        insertBgQuery.executeUpdate();
    }

    @Override
    public void deleteById(long id) {
        Optional<Book> book = findById(id);
        if (book.isEmpty()) {
            throw new EntityNotFoundException("Book for removing with id %d not found".formatted(id));
        }
        entityManager.remove(book.get());
    }

    @Override
    public void deleteByTitle(String title) {
        Optional<Book> book = findByTitle(title);
        if (book.isEmpty()) {
            throw new EntityNotFoundException("Book for removing with title %s not found".formatted(title));
        }
        entityManager.remove(book.get());
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
