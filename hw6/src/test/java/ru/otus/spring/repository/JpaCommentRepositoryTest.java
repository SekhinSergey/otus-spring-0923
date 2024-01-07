package ru.otus.spring.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.model.Comment;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.spring.repository.TestBookUtils.*;

@DataJpaTest
@Import(JpaCommentRepository.class)
class JpaCommentRepositoryTest {

    private static final String SIZE_ASSERTION_RULE = "java:S5838";

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
    @SuppressWarnings(SIZE_ASSERTION_RULE)
    void shouldFindExpectedAllComments() {
        var actualCommentList = jpaCommentRepository.findAll();
        var expectedCommentList = dbComments;
        assertThat(actualCommentList.size()).isEqualTo(expectedCommentList.size());
        var expectedCommentMap = expectedCommentList.stream()
                .collect(Collectors.toMap(Comment::getId, Function.identity()));
        actualCommentList.forEach(actualComment -> {
            var expectedComment = expectedCommentMap.get(actualComment.getId());
            assertThatActualAndExpectedCommentAreEqual(actualComment, expectedComment);
        });
    }

    @ParameterizedTest
    @MethodSource("getDbComments")
    void shouldFindExpectedCommentById(Comment dbComment) {
        long id = dbComment.getId();
        var optionalActualComment = jpaCommentRepository.findById(id);
        var expectedComment = testEntityManager.find(Comment.class, id);
        assertThat(optionalActualComment).isPresent();
        var actualComment = optionalActualComment.get();
        assertThatActualAndExpectedCommentAreEqual(actualComment, expectedComment);
    }

    @ParameterizedTest
    @MethodSource("getDbComments")
    void shouldFindExpectedCommentByText(Comment dbComment) {
        var optionalActualComment = jpaCommentRepository.findByText(dbComment.getText());
        var expectedComment = testEntityManager.find(Comment.class, dbComment.getId());
        assertThat(optionalActualComment).isPresent();
        var actualComment = optionalActualComment.get();
        assertThatActualAndExpectedCommentAreEqual(actualComment, expectedComment);
    }

    @Test
    void shouldFindAllExpectedCommentsByBookId() {
        List<Comment> comments = jpaCommentRepository.findAllByBookId(getDbBooks().get(0).getId());
        var actualComment = comments.get(0);
        var expectedComment = testEntityManager.find(Comment.class, dbComments.get(0).getId());
        assertThatActualAndExpectedCommentAreEqual(actualComment, expectedComment);
    }

    @Test
    void shouldFindAllExpectedCommentsByBookTitle() {
        List<Comment> comments = jpaCommentRepository.findAllByBookTitle(getDbBooks().get(0).getTitle());
        var actualComment = comments.get(0);
        var expectedComment = testEntityManager.find(Comment.class, dbComments.get(0).getId());
        assertThatActualAndExpectedCommentAreEqual(actualComment, expectedComment);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void shouldPersistExpectedComment() {
        shouldSaveExpectedComment(0);
    }

    @Test
    void shouldMergeExpectedComment() {
        shouldSaveExpectedComment(4);
    }

    void shouldSaveExpectedComment(long id) {
        var actualComment = jpaCommentRepository.save(new Comment(id, "Comment_4", getDbBooks().get(0)));
        var expectedComment = testEntityManager.find(Comment.class, 4);
        assertThatActualAndExpectedCommentAreEqual(actualComment, expectedComment);
    }

    private void assertThatActualAndExpectedCommentAreEqual(Comment actualComment, Comment expectedComment) {
        assertThat(actualComment.getId()).isEqualTo(actualComment.getId());
        assertThat(actualComment.getText()).isEqualTo(expectedComment.getText());
        assertThatActualAndExpectedBookAreEqual(actualComment.getBook(), expectedComment.getBook());
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
