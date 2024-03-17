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
import static ru.otus.spring.utils.Utils.FIRST_INDEX;
import static ru.otus.spring.utils.Utils.assertThatActualAndExpectedBookAreEqual;
import static ru.otus.spring.utils.Utils.getDbBooks;
import static ru.otus.spring.utils.Utils.getDbComments;

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
        List<Comment> comments = commentRepository.findAllByBookId(getDbBooks().get(FIRST_INDEX).getId());
        var actualComment = comments.get(FIRST_INDEX);
        var expectedComment = testEntityManager.find(Comment.class, dbComments.get(FIRST_INDEX).getId());
        assertThat(actualComment.getId()).isEqualTo(actualComment.getId());
        assertThat(actualComment.getText()).isEqualTo(expectedComment.getText());
        assertThatActualAndExpectedBookAreEqual(actualComment.getBook(), expectedComment.getBook());
    }

    @Test
    void shouldDeleteCommentsByBookIdCorrectly() {
        Long bookId = getDbBooks().get(FIRST_INDEX).getId();
        commentRepository.deleteAllByBookId(bookId);
        assertThat(commentRepository.findAllByBookId(bookId)).isEmpty();
    }

    @Test
    void shouldDeleteCommentsByBookIdsCorrectly() {
        Long firstBookId = getDbBooks().get(FIRST_INDEX).getId();
        int secondIndex = 1;
        Long secondBookId = getDbBooks().get(secondIndex).getId();
        commentRepository.deleteAllByBookIdIn(Set.of(firstBookId, secondBookId));
        assertThat(commentRepository.findAllByBookId(firstBookId)).isEmpty();
        assertThat(commentRepository.findAllByBookId(secondBookId)).isEmpty();
    }

    @Test
    void shouldCountCommentsByBookIdCorrectly() {
        int singleSize = 1;
        assertThat(commentRepository.countByBookId(getDbBooks().get(FIRST_INDEX).getId())).isEqualTo(singleSize);
    }
}
