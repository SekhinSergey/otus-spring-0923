package ru.otus.spring.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.mapper.BookMapper;
import ru.otus.spring.service.BookService;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.otus.spring.utils.TestBookUtils.getDbBooks;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    @SuppressWarnings("all")
    private BookMapper bookMapper;

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
                .andExpect(content().string(containsString("BookTitle_3")))
                .andExpect(content().string(containsString("Author_1")))
                .andExpect(content().string(containsString("Author_2")))
                .andExpect(content().string(containsString("Author_3")))
                .andExpect(content().string(containsString("Genre_1")))
                .andExpect(content().string(containsString("Genre_2")))
                .andExpect(content().string(containsString("Genre_3")))
                .andExpect(content().string(containsString("Genre_4")))
                .andExpect(content().string(containsString("Genre_5")))
                .andExpect(content().string(containsString("Genre_6")));
    }

    // Не понимаю, как тестировать остальные эндпоинты
}
