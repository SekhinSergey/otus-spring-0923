package ru.otus.spring.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.spring.model.Comment;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.spring.utils.TestBookUtils.*;

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
}
