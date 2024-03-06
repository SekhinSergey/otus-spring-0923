package ru.otus.spring.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.service.AuthorService;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.otus.spring.utils.Utils.getDbAuthors;
import static ru.otus.spring.utils.Utils.getStringJsonByFilePath;

@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @Test
    void getAllTest() throws Exception {
        given(authorService.findAll()).willReturn(getDbAuthors().stream()
                .map(author -> AuthorDto.builder()
                        .id(author.getId())
                        .fullName(author.getFullName())
                        .build())
                .toList());
        this.mockMvc
                .perform(get("/api/library/authors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getStringJsonByFilePath("src/test/resources/json/allAuthors.json")));
    }
}
