package ru.otus.spring.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.spring.model.Comment;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Repository
@RequiredArgsConstructor
public class JpaCommentRepository implements CommentRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Comment> findAll() {
        return entityManager.createQuery("select c from Comment c", Comment.class).getResultList();
    }

    @Override
    public Optional<Comment> findById(long id) {
        TypedQuery<Comment> query = entityManager.createQuery(
                "select c from Comment c where c.id = :id", Comment.class);
        query.setParameter("id", id);
        return getOptionalComment(query.getSingleResult());
    }

    @Override
    public Optional<Comment> findByText(String text) {
        TypedQuery<Comment> query = entityManager.createQuery(
                "select c from Comment c where c.text = :text", Comment.class);
        query.setParameter("text", text);
        return getOptionalComment(query.getSingleResult());
    }

    private static Optional<Comment> getOptionalComment(Comment comment) {
        return isNull(comment) ? Optional.empty() : Optional.of(comment);
    }

    public List<Comment> findAllByBookId(long bookId) {
        TypedQuery<Comment> query = entityManager.createQuery(
                "select c from Comment c where c.book.id = :bookId", Comment.class);
        query.setParameter("bookId", bookId);
        List<Comment> comments = query.getResultList();
        return comments.isEmpty() ? Collections.emptyList() : comments;
    }

    @Override
    public List<Comment> findAllByBookTitle(String title) {
        TypedQuery<Comment> query = entityManager.createQuery(
                "select c from Comment c where c.book.title = :title", Comment.class);
        query.setParameter("title", title);
        List<Comment> comments = query.getResultList();
        return comments.isEmpty() ? Collections.emptyList() : comments;
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == null) {
            entityManager.persist(comment);
            return findByText(comment.getText()).orElse(null);
        }
        return entityManager.merge(comment);
    }

    @Override
    public void deleteAllByBookId(long bookId) {
        Query query = entityManager.createQuery("delete Comment c where c.book.id = :bookId");
        query.setParameter("bookId", bookId);
        query.executeUpdate();
    }

    @Override
    public void deleteAllByBookTitle(String title) {
        Query query = entityManager.createQuery("delete Comment c where c.book.title = :title");
        query.setParameter("title", title);
        query.executeUpdate();
    }

    @Override
    public int countByBookId(long bookId) {
        return findAllByBookId(bookId).size();
    }

    @Override
    public int countByBookTitle(String title) {
        return findAllByBookTitle(title).size();
    }
}
