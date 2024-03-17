package ru.otus.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.dto.create.BookCreateDto;
import ru.otus.spring.dto.update.BookUpdateDto;
import ru.otus.spring.exception.NotFoundException;
import ru.otus.spring.mapper.BookMapper;
import ru.otus.spring.model.Book;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;
import ru.otus.spring.repository.GenreRepository;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static ru.otus.spring.constant.Constants.GENRES_SIZE_ERROR_MESSAGE;
import static ru.otus.spring.utils.Utils.FIRST_INDEX;
import static ru.otus.spring.utils.Utils.NOT_EXISTENT_ID;
import static ru.otus.spring.utils.Utils.NO_AUTHOR_BY_SPECIFIC_ID_ERROR_MESSAGE;
import static ru.otus.spring.utils.Utils.NO_BOOK_BY_SPECIFIC_ID_ERROR_MESSAGE;
import static ru.otus.spring.utils.Utils.getDbAuthors;
import static ru.otus.spring.utils.Utils.getDbBooks;

@Import(BookServiceImpl.class)
@ExtendWith(SpringExtension.class)
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
class NotFoundExceptionBookServiceTest {

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private BookMapper bookMapper;

    @Autowired
    private BookServiceImpl bookService;

    @Test
    void noIdEditTest() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThat(assertThrows(NotFoundException.class, () ->
                bookService.update(NOT_EXISTENT_ID, BookUpdateDto.builder().build())
        ).getMessage()).isEqualTo(NO_BOOK_BY_SPECIFIC_ID_ERROR_MESSAGE);

    }

    @Test
    void noAuthorIdEditTest() {
        Book book = getDbBooks().get(FIRST_INDEX);
        when(bookRepository.findById(anyLong())).thenReturn(Optional.ofNullable(book));
        assertThat(assertThrows(NotFoundException.class, () ->
                bookService.update(book.getId(), BookUpdateDto.builder().authorId(NOT_EXISTENT_ID).build())
        ).getMessage()).isEqualTo(NO_AUTHOR_BY_SPECIFIC_ID_ERROR_MESSAGE);
    }

    @Test
    void noAuthorIdCreateTest() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.ofNullable(getDbBooks().get(FIRST_INDEX)));
        assertThat(assertThrows(NotFoundException.class, () ->
                bookService.create(BookCreateDto.builder().authorId(NOT_EXISTENT_ID).build())
        ).getMessage()).isEqualTo(NO_AUTHOR_BY_SPECIFIC_ID_ERROR_MESSAGE);
    }

    @Test
    void noGenreIdEditTest() {
        Book book = getDbBooks().get(FIRST_INDEX);
        when(bookRepository.findById(anyLong())).thenReturn(Optional.ofNullable(book));
        assertThat(assertThrows(NotFoundException.class, () ->
                bookService.update(book.getId(), BookUpdateDto.builder()
                        .authorId(getDbAuthors().get(FIRST_INDEX).getId())
                        .genreIds(Set.of(NOT_EXISTENT_ID))
                        .build())
        ).getMessage()).isEqualTo(GENRES_SIZE_ERROR_MESSAGE);
    }

    @Test
    void noGenreIdCreateTest() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.ofNullable(getDbBooks().get(FIRST_INDEX)));
        when(authorRepository.findById(anyLong())).thenReturn(Optional.ofNullable(getDbAuthors().get(FIRST_INDEX)));
        assertThat(assertThrows(NotFoundException.class, () ->
                bookService.create(BookCreateDto.builder()
                        .authorId(getDbAuthors().get(FIRST_INDEX).getId())
                        .genreIds(Set.of(NOT_EXISTENT_ID))
                        .build())
        ).getMessage()).isEqualTo(GENRES_SIZE_ERROR_MESSAGE);
    }
}
