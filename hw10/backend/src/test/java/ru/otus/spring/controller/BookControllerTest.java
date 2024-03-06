package ru.otus.spring.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.dto.create.BookCreateDto;
import ru.otus.spring.dto.update.BookUpdateDto;
import ru.otus.spring.model.Book;
import ru.otus.spring.model.Genre;
import ru.otus.spring.service.BookService;

import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.otus.spring.utils.Utils.getDbBooks;
import static ru.otus.spring.utils.Utils.getStringJsonByFilePath;

@WebMvcTest(BookController.class)
class BookControllerTest {

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
        Book book = getDbBooks().get(0);
        when(bookService.update(any())).thenReturn(BookUpdateDto.builder()
                .id(book.getId())
                .title("BookTitle_4")
                .authorId(book.getAuthor().getId())
                .genreIds(book.getGenres().stream()
                        .map(Genre::getId)
                        .collect(toSet()))
                .build());
        String json = getStringJsonByFilePath("src/test/resources/json/book/book_for_update.json");
        this.mockMvc
                .perform(put("/api/library/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    void addTest() throws Exception {
        when(bookService.create(any())).thenReturn(BookCreateDto.builder()
                .id(4L)
                .title("BookTitle_4")
                .authorId(1L)
                .genreIds(Set.of(1L))
                .build());
        this.mockMvc
                .perform(post("/api/library/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getStringJsonByFilePath("src/test/resources/json/book/book_for_creation.json")))
                .andExpect(status().isOk())
                .andExpect(content().json(getStringJsonByFilePath("src/test/resources/json/book/created_book.json")));
    }

    @Test
    void deleteTest() throws Exception {
        this.mockMvc
                .perform(delete("/api/library/book?id=" + getDbBooks().get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Book with id 1 deleted")));
    }

    @Test
    void noIdEditTest() throws Exception {
        this.mockMvc
                .perform(put("/api/library/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getStringJsonByFilePath(
                                "src/test/resources/json/book/validation/no_id_book_for_update.json")))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"id\":\"Book ID value should not be null\"}"));
    }

    @Test
    void noTitleEditTest() throws Exception {
        this.mockMvc
                .perform(put("/api/library/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getStringJsonByFilePath(
                                "src/test/resources/json/book/validation/no_title_book_for_update.json")))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"title\":\"Book title value should not be blank\"}"));
    }

    @Test
    void noAuthorIdEditTest() throws Exception {
        this.mockMvc
                .perform(put("/api/library/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getStringJsonByFilePath(
                                "src/test/resources/json/book/validation/no_author_id_book_for_update.json")))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"authorId\":\"Author ID value should not be null\"}"));
    }

    @Test
    void noGenreIdsEditTest() throws Exception {
        this.mockMvc
                .perform(put("/api/library/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getStringJsonByFilePath(
                                "src/test/resources/json/book/validation/no_genre_ids_book_for_update.json")))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"genreIds\":\"Set of genre IDs value should not be empty\"}"));
    }

    @Test
    void emptyGenreIdsEditTest() throws Exception {
        this.mockMvc
                .perform(put("/api/library/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getStringJsonByFilePath(
                                "src/test/resources/json/book/validation/empty_genre_ids_book_for_update.json")))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"genreIds\":\"Set of genre IDs value should not be empty\"}"));
    }

    @Test
    void noSomeGenreIdEditTest() throws Exception {
        this.mockMvc
                .perform(put("/api/library/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getStringJsonByFilePath(
                                "src/test/resources/json/book/validation/no_some_genre_id_book_for_update.json")))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"genreIds[]\":\"Genre ID value should not be null\"}"));
    }
}
