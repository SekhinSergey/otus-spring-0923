package ru.otus.spring.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.model.Comment;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.spring.repository.TestBookUtils.assertThatActualAndExpectedBookAreEqual;
import static ru.otus.spring.repository.TestBookUtils.getDbBooks;

@DataMongoTest
class CommentRepositoryTest {

    private List<Comment> dbComments;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        dbComments = getDbComments();
    }

    @ParameterizedTest
    @SuppressWarnings("all")
    @MethodSource("getDbComments")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void shouldFindExpectedCommentByText(Comment dbComment) {
        var optionalActualComment = commentRepository.findByText(dbComment.getText());
        var expectedComment = mongoTemplate.findById(dbComment.getId(), Comment.class);
        assertThat(optionalActualComment).isPresent();
        var actualComment = optionalActualComment.get();
        assertThatActualAndExpectedCommentAreEqual(actualComment, expectedComment);
    }

    @Test
    @SuppressWarnings("all")
    void shouldFindAllExpectedCommentsByBookId() {
        List<Comment> comments = commentRepository.findAllByBookId(getDbBooks().get(0).getId());
        var actualComment = comments.get(0);
        var expectedComment = mongoTemplate.findById(dbComments.get(0).getId(), Comment.class);
        assertThatActualAndExpectedCommentAreEqual(actualComment, expectedComment);
    }

    @Test
    @SuppressWarnings("all")
    void shouldFindAllExpectedCommentsByBookTitle() {
        List<Comment> comments = commentRepository.findAllByBookTitle(getDbBooks().get(0).getTitle());
        var actualComment = comments.get(0);
        var expectedComment = mongoTemplate.findById(dbComments.get(0).getId(), Comment.class);
        assertThatActualAndExpectedCommentAreEqual(actualComment, expectedComment);
    }

    private void assertThatActualAndExpectedCommentAreEqual(Comment actualComment, Comment expectedComment) {
        assertThat(actualComment.getId()).isEqualTo(actualComment.getId());
        assertThat(actualComment.getText()).isEqualTo(expectedComment.getText());
        assertThatActualAndExpectedBookAreEqual(actualComment.getBook(), expectedComment.getBook());
    }

    @Test
    void shouldDeleteCommentsByBookIdCorrectly() {
        String bookId = getDbBooks().get(0).getId();
        commentRepository.deleteAllByBookId(bookId);
        assertThat(commentRepository.findAllByBookId(bookId)).isEmpty();
    }

    @Test
    void shouldDeleteCommentsByBookTitleCorrectly() {
        String title = getDbBooks().get(0).getTitle();
        commentRepository.deleteAllByBookTitle(title);
        assertThat(commentRepository.findAllByBookTitle(title)).isEmpty();
    }

    @Test
    void shouldDeleteCommentsByBookIdsCorrectly() {
        String firstBookId = getDbBooks().get(0).getId();
        String secondBookId = getDbBooks().get(1).getId();
        commentRepository.deleteAllByBookIdIn(Set.of(firstBookId, secondBookId));
        assertThat(commentRepository.findAllByBookId(firstBookId)).isEmpty();
        assertThat(commentRepository.findAllByBookId(secondBookId)).isEmpty();
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void shouldCountCommentsByBookIdCorrectly() {
        assertThat(commentRepository.countByBookId(getDbBooks().get(0).getId())).isEqualTo(1);
    }

//    @Test
//    void shouldCountCommentsByBookTitleCorrectly() {
//        assertThat(commentRepository.countByBookTitle(getDbBooks().get(0).getTitle())).isEqualTo(1);
//    }

    private static List<Comment> getDbComments() {
        return IntStream.range(1, 4)
                .boxed()
                .map(id -> new Comment(id.toString(), ("Comment_" + id), getDbBooks().get(id - 1)))
                .toList();
    }
}
