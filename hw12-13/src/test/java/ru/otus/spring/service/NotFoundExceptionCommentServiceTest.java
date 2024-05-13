package ru.otus.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.dto.create.CommentCreateDto;
import ru.otus.spring.dto.update.CommentUpdateDto;
import ru.otus.spring.exception.NotFoundException;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;
import ru.otus.spring.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static ru.otus.spring.utils.Utils.FIRST_INDEX;
import static ru.otus.spring.utils.Utils.NOT_EXISTENT_ID;
import static ru.otus.spring.utils.Utils.NO_BOOK_BY_SPECIFIC_ID_ERROR_MESSAGE;
import static ru.otus.spring.utils.Utils.getDbBooks;
import static ru.otus.spring.utils.Utils.getDbComments;

@Import(CommentServiceImpl.class)
@ExtendWith(SpringExtension.class)
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
class NotFoundExceptionCommentServiceTest {

    private static final String NO_USER_BY_SPECIFIC_ID_ERROR_MESSAGE = "User with ID 7 does not exist";

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private CommentServiceImpl commentService;

    @Test
    void noBookIdEditTest() {
        var comment = getDbComments().get(FIRST_INDEX);
        when(commentRepository.findById(anyLong())).thenReturn(Optional.ofNullable(comment));
        assertThat(assertThrows(NotFoundException.class, () ->
                commentService.update(comment.getId(), CommentUpdateDto.builder().bookId(NOT_EXISTENT_ID).build())
        ).getMessage()).isEqualTo(NO_BOOK_BY_SPECIFIC_ID_ERROR_MESSAGE);
    }

    @Test
    void noUserIdEditTest() {
        var comment = getDbComments().get(FIRST_INDEX);
        when(commentRepository.findById(anyLong())).thenReturn(Optional.ofNullable(comment));
        var book = getDbBooks().get(FIRST_INDEX);
        when(bookRepository.findById(anyLong())).thenReturn(Optional.ofNullable(book));
        assertThat(assertThrows(NotFoundException.class, () ->
                commentService.update(comment.getId(), CommentUpdateDto.builder()
                        .bookId(book.getId())
                        .userId(NOT_EXISTENT_ID)
                        .build())
        ).getMessage()).isEqualTo(NO_USER_BY_SPECIFIC_ID_ERROR_MESSAGE);
    }

    @Test
    void noBookIdCreateTest() {
        assertThat(assertThrows(NotFoundException.class, () ->
                commentService.create(CommentCreateDto.builder().bookId(NOT_EXISTENT_ID).build())
        ).getMessage()).isEqualTo(NO_BOOK_BY_SPECIFIC_ID_ERROR_MESSAGE);
    }

    @Test
    void noUserIdCreateTest() {
        var book = getDbBooks().get(FIRST_INDEX);
        when(bookRepository.findById(anyLong())).thenReturn(Optional.ofNullable(book));
        assertThat(assertThrows(NotFoundException.class, () ->
                commentService.create(CommentCreateDto.builder()
                        .bookId(book.getId())
                        .userId(NOT_EXISTENT_ID)
                        .build())
        ).getMessage()).isEqualTo(NO_USER_BY_SPECIFIC_ID_ERROR_MESSAGE);
    }
}
