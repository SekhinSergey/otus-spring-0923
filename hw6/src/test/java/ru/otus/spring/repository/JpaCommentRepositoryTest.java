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
@Import({JpaCommentRepository.class, JpaBookRepository.class})
class JpaCommentRepositoryTest {

    private List<Comment> dbComments;

    @Autowired
    private JpaCommentRepository jpaCommentRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
        dbComments = getDbComments();
    }

    @Test
    @SuppressWarnings("all")
    void shouldFindExpectedAllComments() {
        var actualComments = jpaCommentRepository.findAll();
        var expectedComments = dbComments;
        IntStream.range(0, actualComments.size())
                .mapToObj(i -> actualComments.get(i).equals(expectedComments.get(i)))
                .forEach(Assertions::assertThat);
    }

    @ParameterizedTest
    @MethodSource("getDbComments")
    void shouldFindExpectedCommentById(Comment dbComment) {
        long id = dbComment.getId();
        var actualComment = jpaCommentRepository.findById(id);
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
        var actualComment = jpaCommentRepository.findByText(dbComment.getText());
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
        List<Comment> comments = jpaCommentRepository.findAllByBookId(getDbBooks().get(0).getId());
        var actualComment = comments.get(0);
        var expectedComment = testEntityManager.find(Comment.class, dbComments.get(0).getId());
        var expectedAuthor = testEntityManager.find(Author.class, getDbAuthors().get(0).getId());
        expectedComment.getBook().setAuthor(expectedAuthor);
        assertThat(actualComment).isEqualTo(expectedComment);
    }

    @Test
    void shouldFindAllExpectedCommentsByBookTitle() {
        List<Comment> comments = jpaCommentRepository.findAllByBookTitle(getDbBooks().get(0).getTitle());
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
        var actualComment = jpaCommentRepository.save(new Comment(id, "Comment_4", getDbBooks().get(0)));
        var expectedComment = testEntityManager.find(Comment.class, 4);
        assertThat(actualComment)
                .usingRecursiveComparison()
                .isEqualTo(expectedComment);
    }

    @Test
    void shouldDeleteCommentsByBookIdCorrectly() {
        Long bookId = getDbBooks().get(0).getId();
        jpaCommentRepository.deleteAllByBookId(bookId);
        assertThat(jpaCommentRepository.findAllByBookId(bookId)).isEmpty();
    }

    @Test
    void shouldDeleteCommentsByBookTitleCorrectly() {
        String title = getDbBooks().get(0).getTitle();
        jpaCommentRepository.deleteAllByBookTitle(title);
        assertThat(jpaCommentRepository.findAllByBookTitle(title)).isEmpty();
    }

    @Test
    void shouldCountCommentsByBookIdCorrectly() {
        assertThat(jpaCommentRepository.countByBookId(getDbBooks().get(0).getId())).isEqualTo(1);
    }

    @Test
    void shouldCountCommentsByBookTitleCorrectly() {
        assertThat(jpaCommentRepository.countByBookTitle(getDbBooks().get(0).getTitle())).isEqualTo(1);
    }

    private static List<Comment> getDbComments() {
        return IntStream.range(1, 4)
                .boxed()
                .map(id -> new Comment((long) id, ("Comment_" + id), getDbBooks().get(id - 1)))
                .toList();
    }
}
