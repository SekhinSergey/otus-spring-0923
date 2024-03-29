package ru.otus.spring.controller;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.dto.create.BookCreateDto;
import ru.otus.spring.dto.update.BookUpdateDto;
import ru.otus.spring.model.Book;
import ru.otus.spring.model.Genre;
import ru.otus.spring.service.BookService;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.otus.spring.utils.TestBookUtils.getDbBooks;

@WebMvcTest(BookController.class)
class BookControllerTest {

    private static final String TEST_BOOK_TITLE = "BookTitle_4";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    void getAllTest() throws Exception {
        given(bookService.findAll()).willReturn(getDbBooks().stream()
                .map(book -> {
                    Set<Long> genreIds = book.getGenres()
                            .stream()
                            .map(Genre::getId)
                            .collect(Collectors.toSet());
                    return BookDto.builder()
                            .id(book.getId())
                            .title(book.getTitle())
                            .authorId(book.getAuthor().getId())
                            .genreIds(genreIds)
                            .build();
                })
                .toList());
        this.mockMvc
                .perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("1")))
                .andExpect(content().string(containsString("2")))
                .andExpect(content().string(containsString("3")))
                .andExpect(content().string(containsString("BookTitle_1")))
                .andExpect(content().string(containsString("BookTitle_2")))
                .andExpect(content().string(containsString("BookTitle_3")));
    }

    @Test
    void editTest() throws Exception {
        Book book = getDbBooks().get(0);
        String genreIds = book.getGenres()
                .stream()
                .map(genre -> genre.getId().toString() + SPACE)
                .collect(Collectors.joining())
                .trim();
        Long id = book.getId();
        BookUpdateDto bookUpdateDto = BookUpdateDto.builder()
                .id(id)
                .title(TEST_BOOK_TITLE)
                .authorId(book.getAuthor().getId())
                .genreIds(genreIds)
                .build();
        when(bookService.findByIdForEditing(anyLong())).thenReturn(bookUpdateDto);
        this.mockMvc
                .perform(get("/editBook?id=" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(TEST_BOOK_TITLE)));
    }

    @Test
    void addTest() throws Exception {
        when(bookService.isTest()).thenReturn(true);
        Book firstBook = getDbBooks().get(0);
        String genreIds = firstBook.getGenres()
                .stream()
                .map(genre -> genre.getId().toString() + SPACE)
                .collect(Collectors.joining())
                .trim();
        BookCreateDto bookCreateDto = BookCreateDto.builder()
                .id(firstBook.getId())
                .title(TEST_BOOK_TITLE)
                .authorId(firstBook.getAuthor().getId())
                .genreIds(genreIds)
                .build();
        when(bookService.create(any())).thenReturn(bookCreateDto);
        this.mockMvc
                .perform(post("/addBook"))
                .andExpect(status().is3xxRedirection());
        Book newBook = Book.builder()
                .id(bookCreateDto.getId())
                .title(TEST_BOOK_TITLE)
                .author(firstBook.getAuthor())
                .genres(firstBook.getGenres())
                .build();
        given(bookService.findAll()).willReturn(Stream.of(
                        getDbBooks().get(0),
                        getDbBooks().get(1),
                        getDbBooks().get(2),
                        newBook)
                .map(book -> {
                    Set<Long> genreIdList = book.getGenres()
                            .stream()
                            .map(Genre::getId)
                            .collect(Collectors.toSet());
                    return BookDto.builder()
                            .id(book.getId())
                            .title(book.getTitle())
                            .authorId(book.getAuthor().getId())
                            .genreIds(genreIdList)
                            .build();
                })
                .toList());
        this.mockMvc
                .perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(TEST_BOOK_TITLE)));
    }

    @Test
    void deleteTest() throws Exception {
        Book firstBook = getDbBooks().get(0);
        this.mockMvc
                .perform(post("/deleteBook?id=" + firstBook.getId()))
                .andExpect(status()
                        .is3xxRedirection());
        given(bookService.findAll()).willReturn(Stream.of(getDbBooks().get(1), getDbBooks().get(2))
                .map(book -> {
                    Set<Long> genreIds = book.getGenres()
                            .stream()
                            .map(Genre::getId)
                            .collect(Collectors.toSet());
                    return BookDto.builder()
                            .id(book.getId())
                            .title(book.getTitle())
                            .authorId(book.getAuthor().getId())
                            .genreIds(genreIds)
                            .build();
                })
                .toList());
        this.mockMvc
                .perform(get("/")).andExpect(status().isOk())
                .andExpect(content().string(doesNotContainsString(firstBook.getTitle())));
    }

    private static Matcher<String> doesNotContainsString(String s) {
        return CoreMatchers.not(containsString(s));
    }
}
