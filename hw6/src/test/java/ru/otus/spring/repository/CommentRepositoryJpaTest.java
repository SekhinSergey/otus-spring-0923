package ru.otus.spring.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.spring.model.Book;
import ru.otus.spring.model.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(CommentRepositoryJpa.class)
class CommentRepositoryJpaTest {

    @Autowired
    private CommentRepositoryJpa commentRepositoryJpa;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void shouldFindAllExpectedCommentsByBookId() {
        List<Comment> comments = commentRepositoryJpa.findAllByBookId(1);
        var actualComment = comments.get(0);
        var expectedComment = testEntityManager.find(Comment.class, 1);
        var bookExample = testEntityManager.find(Book.class, 1);
        assertThat(actualComment).isEqualTo(expectedComment);
    }
}
