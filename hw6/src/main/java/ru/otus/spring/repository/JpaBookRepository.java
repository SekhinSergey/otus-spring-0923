package ru.otus.spring.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.spring.exception.EntityNotFoundException;
import ru.otus.spring.model.Book;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Repository
@RequiredArgsConstructor
public class JpaBookRepository implements BookRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<Book> findByTitle(String title) {
        TypedQuery<Book> query = entityManager.createQuery(
                "select b from Book b where b.title = :title", Book.class);
        query.setParameter("title", title);
        return getOptionalBook(query);
    }

    @Override
    public Optional<Book> findById(long id) {
        TypedQuery<Book> query = entityManager.createQuery(
                "select b from Book b where b.id = :id", Book.class);
        query.setParameter("id", id);
        return getOptionalBook(query);
    }

    // Была рекомендация удалить метод, но это нельзя сделать, так как упадут тесты
    private static Optional<Book> getOptionalBook(TypedQuery<Book> query) {
        Book book;
        try {
            book = query.getSingleResult();
        } catch (NoResultException exception) {
            book = null;
        }
        return Optional.ofNullable(book);
    }

    @Override
    public List<Book> findAll() {
        return entityManager.createQuery("select b from Book b", Book.class).getResultList();
    }

    @Override
    public Book save(Book book) {
        return book.getId() == null ? insert(book) : entityManager.merge(book);
    }

    // Была рекомендация заинлайнить. Ответил, что персист не возвращает ничего. Снова ее получил
    // Не знаю, как на это реагировать
    private Book insert(Book book) {
        entityManager.persist(book);
        return book;
    }

    @Override
    public void deleteById(long id) {
        Book book = entityManager.find(Book.class, id);
        if (isNull(book)) {
            throw new EntityNotFoundException("Book for removing with id %d not found".formatted(id));
        }
        entityManager.remove(book);
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
    @SuppressWarnings("all")
    public int countByAuthorId(long authorId) {
        Query nativeQuery = entityManager.createNativeQuery(
                "select count(*) from books b where b.author_id = :authorId");
        nativeQuery.setParameter("authorId", authorId);
        return Integer.parseInt(nativeQuery.getSingleResult().toString());
    }

    @Override
    @SuppressWarnings("all")
    public int countByAuthorFullName(String authorFullName) {
        Query nativeQuery = entityManager.createNativeQuery(
                "select count(*) " +
                        "from books b " +
                        "join authors a on b.author_id = a.id " +
                        "where a.full_name = :authorFullName");
        nativeQuery.setParameter("authorFullName", authorFullName);
        return Integer.parseInt(nativeQuery.getSingleResult().toString());
    }

    @Override
    @SuppressWarnings("all")
    public int countByGenreId(long genreId) {
        Query nativeQuery = entityManager.createNativeQuery(
                "select count(*) " +
                        "from books b " +
                        "join authors a on a.id = b.author_id " +
                        "join books_genres bg on bg.book_id = b.id " +
                        "join genres g on g.id = bg.genre_id " +
                        "where g.id = :genreId");
        nativeQuery.setParameter("genreId", genreId);
        return Integer.parseInt(nativeQuery.getSingleResult().toString());
    }

    @Override
    @SuppressWarnings("all")
    public int countByGenreName(String genreName) {
        Query nativeQuery = entityManager.createNativeQuery(
                "select count(*) " +
                        "from books b " +
                        "join authors a on a.id = b.author_id " +
                        "join books_genres bg on bg.book_id = b.id " +
                        "join genres g on g.id = bg.genre_id " +
                        "where g.name = :genreName");
        nativeQuery.setParameter("genreName", genreName);
        return Integer.parseInt(nativeQuery.getSingleResult().toString());
    }
}
