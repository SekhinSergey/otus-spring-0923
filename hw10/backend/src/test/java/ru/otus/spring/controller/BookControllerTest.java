package ru.otus.spring.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.dto.response.BookDto;
import ru.otus.spring.model.Book;
import ru.otus.spring.model.Genre;
import ru.otus.spring.service.BookService;

import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.otus.spring.utils.Utils.FIRST_INDEX;
import static ru.otus.spring.utils.Utils.getDbAuthors;
import static ru.otus.spring.utils.Utils.getDbBooks;
import static ru.otus.spring.utils.Utils.getDbGenres;
import static ru.otus.spring.utils.Utils.getStringJsonByFilePath;

@WebMvcTest(BookController.class)
class BookControllerTest {

    private static final String NEW_TITLE = "BookTitle_4";

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
                            .collect(toSet());
                    return BookDto.builder()
                            .id(book.getId())
                            .title(book.getTitle())
                            .authorId(book.getAuthor().getId())
                            .genreIds(genreIds)
                            .build();
                })
                .toList());
        this.mockMvc
                .perform(get("/api/library/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getStringJsonByFilePath("src/test/resources/json/book/allBooks.json")));
    }

    @Test
    void editTest() throws Exception {
        Book book = getDbBooks().get(FIRST_INDEX);
        long id = book.getId();
        when(bookService.update(anyLong(), any())).thenReturn(BookDto.builder()
                .id(id)
                .title(NEW_TITLE)
                .authorId(book.getAuthor().getId())
                .genreIds(book.getGenres().stream()
                        .map(Genre::getId)
                        .collect(toSet()))
                .build());
        String json = getStringJsonByFilePath("src/test/resources/json/book/book_for_update.json");
        this.mockMvc
                .perform(put("/api/library/book/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().json(json));
    }

    @Test
    void addTest() throws Exception {
        long nextId = 4L;
        when(bookService.create(any())).thenReturn(BookDto.builder()
                .id(nextId)
                .title(NEW_TITLE)
                .authorId(getDbAuthors().get(FIRST_INDEX).getId())
                .genreIds(Set.of(getDbGenres().get(FIRST_INDEX).getId()))
                .build());
        this.mockMvc
                .perform(post("/api/library/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getStringJsonByFilePath("src/test/resources/json/book/book_for_creation.json")))
                .andExpect(status().isCreated())
                .andExpect(content().json(getStringJsonByFilePath("src/test/resources/json/book/created_book.json")));
    }

    @Test
    void deleteTest() throws Exception {
        this.mockMvc
                .perform(delete("/api/library/book?id=" + getDbBooks().get(FIRST_INDEX).getId()))
                .andExpect(status().isNoContent())
                .andExpect(content().string(EMPTY));
    }

    @Test
    void noGenreIdsAllFieldsFailedEditTest() throws Exception {
        this.mockMvc
                .perform(put("/api/library/book/{id}", getDbBooks().get(FIRST_INDEX).getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getStringJsonByFilePath("src/test/resources/json/empty.json")))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(getStringJsonByFilePath(
                        "src/test/resources/json/book/validation/response/no_genre_ids_all_fields_failed.json")));
    }

    @Test
    void emptyGenreIdsAllFieldsFailedEditTest() throws Exception {
        this.mockMvc
                .perform(put("/api/library/book/{id}", getDbBooks().get(FIRST_INDEX).getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getStringJsonByFilePath(
                                "src/test/resources/json/book/validation/request/empty_genre_ids.json")))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(getStringJsonByFilePath(
                        "src/test/resources/json/book/validation/response/no_genre_ids_all_fields_failed.json")));
    }

    @Test
    void noSomeGenreIdAllFieldsFailedEditTest() throws Exception {
        this.mockMvc
                .perform(put("/api/library/book/{id}", getDbBooks().get(FIRST_INDEX).getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getStringJsonByFilePath(
                                "src/test/resources/json/book/validation/request/no_some_genre_id.json")))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(getStringJsonByFilePath(
                        "src/test/resources/json/book/validation/response/no_some_genre_id_all_fields_failed.json")));
    }
}
