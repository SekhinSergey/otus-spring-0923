package ru.otus.spring.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.model.Author;
import ru.otus.spring.model.Comment;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.spring.repository.TestBookUtils.getDbAuthors;
import static ru.otus.spring.repository.TestBookUtils.getDbBooks;

@DataJpaTest
@Import({CommentRepositoryJpa.class, BookRepositoryJpa.class})
class CommentRepositoryJpaTest {

    private List<Comment> dbComments;

    @Autowired
    private CommentRepositoryJpa commentRepositoryJpa;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
        dbComments = getDbComments();
    }

    @Test
    @SuppressWarnings("all")
    void shouldFindExpectedAllComments() {
        var actualComments = commentRepositoryJpa.findAll();
        var expectedComments = dbComments;
        IntStream.range(0, actualComments.size())
                .mapToObj(i -> actualComments.get(i).equals(expectedComments.get(i)))
                .forEach(Assertions::assertThat);
    }

    @ParameterizedTest
    @MethodSource("getDbComments")
    void shouldFindExpectedCommentById(Comment dbComment) {
        long id = dbComment.getId();
        var actualComment = commentRepositoryJpa.findById(id);
        var expectedComment = testEntityManager.find(Comment.class, id);
        var expectedAuthor = testEntityManager.find(Author.class, dbComment.getBook().getAuthor().getId());
        expectedComment.getBook().setAuthor(expectedAuthor);
        assertThat(actualComment)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedComment);
    }

    @ParameterizedTest
    @MethodSource("getDbComments")
    void shouldFindExpectedCommentByText(Comment dbComment) {
        var actualComment = commentRepositoryJpa.findByText(dbComment.getText());
        var expectedComment = testEntityManager.find(Comment.class, dbComment.getId());
        var expectedAuthor = testEntityManager.find(Author.class, dbComment.getBook().getAuthor().getId());
        expectedComment.getBook().setAuthor(expectedAuthor);
        assertThat(actualComment)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedComment);
    }

    @Test
    void shouldFindAllExpectedCommentsByBookId() {
        List<Comment> comments = commentRepositoryJpa.findAllByBookId(getDbBooks().get(0).getId());
        var actualComment = comments.get(0);
        var expectedComment = testEntityManager.find(Comment.class, dbComments.get(0).getId());
        var expectedAuthor = testEntityManager.find(Author.class, getDbAuthors().get(0).getId());
        expectedComment.getBook().setAuthor(expectedAuthor);
        assertThat(actualComment).isEqualTo(expectedComment);
    }

    @Test
    void shouldFindAllExpectedCommentsByBookTitle() {
        List<Comment> comments = commentRepositoryJpa.findAllByBookTitle(getDbBooks().get(0).getTitle());
        var actualComment = comments.get(0);
        var expectedComment = testEntityManager.find(Comment.class, dbComments.get(0).getId());
        var expectedAuthor = testEntityManager.find(Author.class, getDbAuthors().get(0).getId());
        expectedComment.getBook().setAuthor(expectedAuthor);
        assertThat(actualComment).isEqualTo(expectedComment);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void shouldPersistExpectedComment() {
        shouldInsertExpectedComment(0);
    }

    @Test
    void shouldMergeExpectedComment() {
        shouldInsertExpectedComment(4);
    }

    void shouldInsertExpectedComment(long id) {
        var actualComment = commentRepositoryJpa.insert(new Comment(id, "Comment_4", getDbBooks().get(0)));
        var expectedComment = testEntityManager.find(Comment.class, 4);
        assertThat(actualComment)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedComment);
    }

    @Test
    @SuppressWarnings("all")
    void shouldUpdateCommentsByIdCorrectly() {
        var oldComment = testEntityManager.find(Comment.class, dbComments.get(0).getId());
        var actualComment = commentRepositoryJpa.updateTextById(
                dbComments.get(0).getId(), "Comment_4").get();
        assertThat(actualComment).isNotEqualTo(oldComment);
        var newComment = testEntityManager.find(Comment.class, dbComments.get(0).getId());
        assertThat(actualComment).isEqualTo(newComment);
    }

    @Test
    @SuppressWarnings("all")
    void shouldUpdateCommentsByBookIdCorrectly() {
        var oldComment = testEntityManager.find(Comment.class, dbComments.get(0).getId());
        var actualComment = commentRepositoryJpa.updateTextByBookId(
                getDbBooks().get(0).getId(), "Comment_4").get();
        assertThat(actualComment).isNotEqualTo(oldComment);
        var newComment = testEntityManager.find(Comment.class, dbComments.get(0).getId());
        assertThat(actualComment).isEqualTo(newComment);
    }

    @Test
    @SuppressWarnings("all")
    void shouldUpdateCommentsByBookTitleCorrectly() {
        var oldComment = testEntityManager.find(Comment.class, dbComments.get(0).getId());
        var actualComment = commentRepositoryJpa.updateTextByBookTitle(
                getDbBooks().get(0).getTitle(), "Comment_4").get();
        assertThat(actualComment).isNotEqualTo(oldComment);
        var newComment = testEntityManager.find(Comment.class, dbComments.get(0).getId());
        assertThat(actualComment).isEqualTo(newComment);
    }

    @Test
    void shouldDeleteCommentsByBookIdCorrectly() {
        Long bookId = getDbBooks().get(0).getId();
        commentRepositoryJpa.deleteAllByBookId(bookId);
        assertThat(commentRepositoryJpa.findAllByBookId(bookId)).isEmpty();
    }

    @Test
    void shouldDeleteCommentsByBookTitleCorrectly() {
        String title = getDbBooks().get(0).getTitle();
        commentRepositoryJpa.deleteAllByBookTitle(title);
        assertThat(commentRepositoryJpa.findAllByBookTitle(title)).isEmpty();
    }

    @Test
    void shouldCountCommentsByBookIdCorrectly() {
        assertThat(commentRepositoryJpa.countByBookId(getDbBooks().get(0).getId())).isEqualTo(1);
    }

    @Test
    void shouldCountCommentsByBookTitleCorrectly() {
        assertThat(commentRepositoryJpa.countByBookTitle(getDbBooks().get(0).getTitle())).isEqualTo(1);
    }

    private static List<Comment> getDbComments() {
        return IntStream.range(1, 4)
                .boxed()
                .map(id -> new Comment((long) id, ("Comment_" + id), getDbBooks().get(id - 1)))
                .toList();
    }
}
