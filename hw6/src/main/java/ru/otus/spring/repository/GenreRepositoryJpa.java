package ru.otus.spring.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.spring.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.isNull;

@Repository
@RequiredArgsConstructor
public class GenreRepositoryJpa implements GenreRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Genre> findAll() {
        return entityManager.createQuery("select g from Genre g", Genre.class).getResultList();
    }

    @Override
    public List<Genre> findAllByIds(Set<Long> ids) {
        TypedQuery<Genre> query = entityManager.createQuery(
                "select g from Genre g where g.id in (:ids)", Genre.class);
        query.setParameter("ids", ids);
        return query.getResultList();
    }

    @Override
    public Optional<Genre> findByName(String name) {
        TypedQuery<Genre> query = entityManager.createQuery(
                "select g from Genre g where g.name = :name", Genre.class);
        query.setParameter("name", name);
        return getOptionalGenre(query.getSingleResult());
    }

    @Override
    public Optional<Genre> insert(Genre genre) {
        if (genre.getId() == 0) {
            entityManager.persist(genre);
            return findByName(genre.getName());
        }
        return getOptionalGenre(entityManager.merge(genre));
    }

    private static Optional<Genre> getOptionalGenre(Genre genre) {
        return isNull(genre) ? Optional.empty() : Optional.of(genre);
    }
}
