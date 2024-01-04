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

@Repository
@RequiredArgsConstructor
public class JpaGenreRepository implements GenreRepository {

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
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public Genre save(Genre genre) {
        if (genre.getId() == null) {
            entityManager.persist(genre);
            return genre;
        }
        return entityManager.merge(genre);
    }
}
