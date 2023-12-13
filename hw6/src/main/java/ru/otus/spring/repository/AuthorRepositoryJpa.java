package ru.otus.spring.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.spring.model.Author;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Repository
@RequiredArgsConstructor
public class AuthorRepositoryJpa implements AuthorRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Author> findAll() {
        return entityManager.createQuery("select a from Author a", Author.class).getResultList();
    }

    @Override
    public Optional<Author> findById(long id) {
        return getOptionalAuthor(entityManager.find(Author.class, id));
    }

    @Override
    public Optional<Author> findByFullName(String fullName) {
        TypedQuery<Author> query = entityManager.createQuery(
                "select a from Author a where a.fullName = :fullName", Author.class);
        query.setParameter("fullName", fullName);
        return getOptionalAuthor(query.getSingleResult());
    }

    @Override
    public Optional<Author> insert(Author author) {
        if (author.getId() == 0) {
            entityManager.persist(author);
            return findByFullName(author.getFullName());
        }
        return getOptionalAuthor(entityManager.merge(author));
    }

    private static Optional<Author> getOptionalAuthor(Author author) {
        return isNull(author) ? Optional.empty() : Optional.of(author);
    }
}
