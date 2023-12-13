package ru.otus.spring.repository;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.spring.model.Book;
import ru.otus.spring.model.Comment;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryJpa implements CommentRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Comment> findAll() {
        return entityManager.createQuery("select c from Comment c", Comment.class).getResultList();
    }

    @Override
    public Optional<Comment> findById(long id) {
        return getOptionalComment(entityManager.find(Comment.class, id));
    }

    @Override
    public Optional<Comment> findByText(String text) {
        TypedQuery<Comment> query = entityManager.createQuery(
                "select c from Comment c where c.text = :text", Comment.class);
        query.setParameter("text", text);
        return getOptionalComment(query.getSingleResult());
    }

    @Override
    public List<Comment> findAllByBookId(long bookId) {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph("comment-entity-graph");
        TypedQuery<Comment> query = entityManager.createQuery(
                "select c from Comment c where c.book.id = :bookId", Comment.class);
        query.setParameter("bookId", bookId);
        query.setHint(FETCH.getKey(), entityGraph);
        return query.getResultList();
    }

    @Override
    public Optional<Comment> insert(Comment comment) {
        return Optional.empty();
    }

    @Override
    public void updateTextById(long id, String text) {

    }

    @Override
    public void deleteById(long id) {

    }

    private static Optional<Comment> getOptionalComment(Comment comment) {
        return isNull(comment) ? Optional.empty() : Optional.of(comment);
    }
}
