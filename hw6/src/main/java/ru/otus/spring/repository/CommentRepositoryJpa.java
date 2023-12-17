package ru.otus.spring.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.spring.exception.EntityNotFoundException;
import ru.otus.spring.exception.ParseException;
import ru.otus.spring.model.Author;
import ru.otus.spring.model.Book;
import ru.otus.spring.model.Comment;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryJpa implements CommentRepository {

    private static final String COMMENT_ENTITY_GRAPH = "comment-entity-graph";

    // Так и не понял, что не так с контекстом этого репозитория
    // Почему автор пуст в findAll, findById, findByText?
    @PersistenceContext
    private final EntityManager entityManager;

    private final BookRepository bookRepository;

    @Override
    public List<Comment> findAll() {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph(COMMENT_ENTITY_GRAPH);
        TypedQuery<Comment> query = entityManager.createQuery("select c from Comment c", Comment.class);
        query.setHint(FETCH.getKey(), entityGraph);
        return postProcessAllComments(query.getResultList());
    }

    private List<Comment> postProcessAllComments(List<Comment> comments) {
        Set<Book> bookSet = comments.stream()
                .map(Comment::getBook)
                .collect(Collectors.toSet());
        // Тут авторов нет, в сервисе и тесте книг есть
        List<Book> books = bookRepository.findAll();
        if (bookSet.size() != books.size()) {
            throw new EntityNotFoundException(
                    "The number of requested books does not match the number in the database");
        }
        Map<Long, Author> authorMapByBookId = books.stream()
                .collect(Collectors.toMap(Book::getId, Book::getAuthor, (a, b) -> b));
        comments.stream()
                .map(Comment::getBook)
                .forEach(book -> book.setAuthor(authorMapByBookId.get(book.getId())));
        return comments;
    }

    @Override
    public Optional<Comment> findById(long id) {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph(COMMENT_ENTITY_GRAPH);
        TypedQuery<Comment> query = entityManager.createQuery(
                "select c from Comment c where c.id = :id", Comment.class);
        query.setParameter("id", id);
        query.setHint(FETCH.getKey(), entityGraph);
        return getOptionalComment(query.getSingleResult());
    }

    @Override
    public Optional<Comment> findByText(String text) {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph(COMMENT_ENTITY_GRAPH);
        TypedQuery<Comment> query = entityManager.createQuery(
                "select c from Comment c where c.text = :text", Comment.class);
        query.setParameter("text", text);
        query.setHint(FETCH.getKey(), entityGraph);
        return getOptionalComment(query.getSingleResult());
    }

    private Optional<Comment> getOptionalComment(Comment comment) {
        if (nonNull(comment) && nonNull(comment.getBook())) {
            Book book = comment.getBook();
            book.setAuthor(getAuthor(book.getId()));
        } else {
            return Optional.empty();
        }
        // По выходу из репо теряем автора
        return Optional.of(comment);
    }

    public List<Comment> findAllByBookId(long bookId) {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph(COMMENT_ENTITY_GRAPH);
        TypedQuery<Comment> query = entityManager.createQuery(
                "select c from Comment c where c.book.id = :bookId", Comment.class);
        query.setParameter("bookId", bookId);
        query.setHint(FETCH.getKey(), entityGraph);
        List<Comment> comments = query.getResultList();
        if (comments.isEmpty()) {
            return Collections.emptyList();
        }
        Author author = getAuthor(bookId);
        comments.forEach(comment -> comment.getBook().setAuthor(author));
        return comments;
    }

    @Override
    public List<Comment> findAllByBookTitle(String title) {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph(COMMENT_ENTITY_GRAPH);
        TypedQuery<Comment> query = entityManager.createQuery(
                "select c from Comment c where c.book.title = :title", Comment.class);
        query.setParameter("title", title);
        query.setHint(FETCH.getKey(), entityGraph);
        List<Comment> comments = query.getResultList();
        if (comments.isEmpty()) {
            return Collections.emptyList();
        }
        Author author = getAuthor(comments.get(0).getBook().getId());
        comments.forEach(comment -> comment.getBook().setAuthor(author));
        return comments;
    }

    @SuppressWarnings("all")
    private Author getAuthor(long bookId) {
        Query nativeQuery = entityManager.createNativeQuery("select a.id, a.full_name " +
                        "from authors a join books b on a.id = b.author_id where b.id = :bookId");
        nativeQuery.setParameter("bookId", bookId);
        Object objectAuthor = nativeQuery.getSingleResult();
        Author author;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String stringAuthor = objectMapper.writeValueAsString(objectAuthor);
            var authorFields = objectMapper.readValue(stringAuthor, List.class);
            author = Author.builder()
                    .id(Long.parseLong(authorFields.get(0).toString()))
                    .fullName(authorFields.get(1).toString())
                    .build();
        } catch (JsonProcessingException exception) {
            throw new ParseException("Can not get author from object");
        }
        return author;
    }

    @Override
    public Optional<Comment> insert(Comment comment) {
        if (comment.getId() == 0) {
            entityManager.persist(comment);
            return findByText(comment.getText());
        }
        Comment returnedComment = entityManager.merge(comment);
        return isNull(returnedComment) ? Optional.empty() : Optional.of(returnedComment);
    }

    @Override
    public Optional<Comment> updateTextById(long id, String text) {
        Query query = entityManager.createQuery(
                "update Comment c set c.text = :text where c.id = :id");
        query.setParameter("id", id);
        query.setParameter("text", text);
        query.executeUpdate();
        entityManager.clear();
        return findByText(text);
    }

    @Override
    public Optional<Comment> updateTextByBookId(long bookId, String text) {
        Query query = entityManager.createQuery(
                "update Comment c set c.text = :text where c.book.id = :bookId");
        query.setParameter("bookId", bookId);
        query.setParameter("text", text);
        query.executeUpdate();
        entityManager.clear();
        return findByText(text);
    }

    @Override
    public Optional<Comment> updateTextByBookTitle(String title, String text) {
        Query query = entityManager.createQuery(
                "update Comment c set c.text = :text where c.book.title = :title");
        query.setParameter("title", title);
        query.setParameter("text", text);
        query.executeUpdate();
        entityManager.clear();
        return findByText(text);
    }

    @Override
    public void deleteAllByBookId(long bookId) {
        List<Comment> comments = findAllByBookId(bookId);
        if (isNull(comments) || comments.isEmpty()) {
            throw new EntityNotFoundException("Comments for removing with book id %d not found".formatted(bookId));
        }
        Query query = entityManager.createQuery("delete Comment c where c.book.id = :bookId");
        query.setParameter("bookId", bookId);
        query.executeUpdate();
    }

    @Override
    public void deleteAllByBookTitle(String title) {
        List<Comment> comments = findAllByBookTitle(title);
        if (isNull(comments) || comments.isEmpty()) {
            throw new EntityNotFoundException("Comments for removing with book title %s not found".formatted(title));
        }
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
