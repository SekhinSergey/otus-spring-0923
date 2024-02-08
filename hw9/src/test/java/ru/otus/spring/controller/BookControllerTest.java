package ru.otus.spring.controller;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.mapper.BookMapper;
import ru.otus.spring.model.Book;
import ru.otus.spring.model.Genre;
import ru.otus.spring.service.BookDtoService;
import ru.otus.spring.service.BookService;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
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

    @MockBean
    private BookMapper bookMapper;

    @MockBean
    @SuppressWarnings("all")
    private BookDtoService bookDtoService;

    @Test
    void getAllTest() throws Exception {
        given(bookService.findAll()).willReturn(getDbBooks());
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
        Long id = book.getId();
        Set<Long> genreIds = book.getGenres().stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());
        when(bookMapper.toDto(any())).thenReturn(BookDto.builder()
                .id(id)
                .title(TEST_BOOK_TITLE)
                .authorId(book.getAuthor().getId())
                .genreIds(genreIds)
                .build());
        this.mockMvc
                .perform(get("/editBook?id=" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(TEST_BOOK_TITLE)));
    }

    @Test
    void addTest() throws Exception {
        when(bookDtoService.isTest()).thenReturn(true);
        Book book = getDbBooks().get(0);
        Book createdBook = Book.builder()
                .title(TEST_BOOK_TITLE)
                .author(book.getAuthor())
                .genres(book.getGenres())
                .build();
        when(bookMapper.toEntity(any(), any(), anySet())).thenReturn(createdBook);
        this.mockMvc
                .perform(post("/addBook"))
                .andExpect(status().is3xxRedirection());
        given(bookService.findAll()).willReturn(Arrays.asList(
                createdBook,
                getDbBooks().get(1),
                getDbBooks().get(2)));
        this.mockMvc
                .perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(TEST_BOOK_TITLE)));
    }

    @Test
    void deleteTest() throws Exception {
        Book book = getDbBooks().get(0);
        this.mockMvc
                .perform(post("/deleteBook?id=" + book.getId()))
                .andExpect(status()
                        .is3xxRedirection());
        given(bookService.findAll()).willReturn(Arrays.asList(
                getDbBooks().get(1),
                getDbBooks().get(2)));
        this.mockMvc
                .perform(get("/")).andExpect(status().isOk())
                .andExpect(content().string(doesNotContainsString(book.getTitle())));
    }

    private static Matcher<String> doesNotContainsString(String s) {
        return CoreMatchers.not(containsString(s));
    }
}
