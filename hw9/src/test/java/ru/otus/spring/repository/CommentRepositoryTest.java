package ru.otus.spring.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.spring.model.Comment;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.spring.repository.TestBookUtils.*;

@DataJpaTest
class CommentRepositoryTest {

    private List<Comment> dbComments;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
        dbComments = getDbComments();
    }

    @Test
    void shouldFindAllExpectedCommentsByBookId() {
        List<Comment> comments = commentRepository.findAllByBookId(getDbBooks().get(0).getId());
        var actualComment = comments.get(0);
        var expectedComment = testEntityManager.find(Comment.class, dbComments.get(0).getId());
        assertThat(actualComment.getId()).isEqualTo(actualComment.getId());
        assertThat(actualComment.getText()).isEqualTo(expectedComment.getText());
        assertThatActualAndExpectedBookAreEqual(actualComment.getBook(), expectedComment.getBook());
    }

    @Test
    void shouldDeleteCommentsByBookIdCorrectly() {
        Long bookId = getDbBooks().get(0).getId();
        commentRepository.deleteAllByBookId(bookId);
        assertThat(commentRepository.findAllByBookId(bookId)).isEmpty();
    }

    @Test
    void shouldDeleteCommentsByBookIdsCorrectly() {
        Long firstBookId = getDbBooks().get(0).getId();
        Long secondBookId = getDbBooks().get(1).getId();
        commentRepository.deleteAllByBookIdIn(Set.of(firstBookId, secondBookId));
        assertThat(commentRepository.findAllByBookId(firstBookId)).isEmpty();
        assertThat(commentRepository.findAllByBookId(secondBookId)).isEmpty();
    }

    @Test
    void shouldCountCommentsByBookIdCorrectly() {
        assertThat(commentRepository.countByBookId(getDbBooks().get(0).getId())).isEqualTo(1);
    }

    private static List<Comment> getDbComments() {
        return IntStream.range(1, 4)
                .boxed()
                .map(id -> new Comment((long) id, ("Comment_" + id), getDbBooks().get(id - 1)))
                .toList();
    }
}
