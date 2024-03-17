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
import ru.otus.spring.mapper.CommentMapper;
import ru.otus.spring.model.Comment;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static ru.otus.spring.utils.Utils.FIRST_INDEX;
import static ru.otus.spring.utils.Utils.NOT_EXISTENT_ID;
import static ru.otus.spring.utils.Utils.NO_BOOK_BY_SPECIFIC_ID_ERROR_MESSAGE;
import static ru.otus.spring.utils.Utils.getDbComments;

@Import(CommentServiceImpl.class)
@ExtendWith(SpringExtension.class)
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
class NotFoundExceptionCommentServiceTest {

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private CommentMapper commentMapper;

    @Autowired
    private CommentServiceImpl commentService;

    @Test
    void noBookIdEditTest() {
        Comment comment = getDbComments().get(FIRST_INDEX);
        when(commentRepository.findById(anyLong())).thenReturn(Optional.ofNullable(comment));
        assertThat(assertThrows(NotFoundException.class, () ->
                commentService.update(comment.getId(), CommentUpdateDto.builder().bookId(NOT_EXISTENT_ID).build())
        ).getMessage()).isEqualTo(NO_BOOK_BY_SPECIFIC_ID_ERROR_MESSAGE);
    }

    @Test
    void noBookIdCreateTest() {
        assertThat(assertThrows(NotFoundException.class, () ->
                commentService.create(CommentCreateDto.builder().bookId(NOT_EXISTENT_ID).build())
        ).getMessage()).isEqualTo(NO_BOOK_BY_SPECIFIC_ID_ERROR_MESSAGE);
    }
}
