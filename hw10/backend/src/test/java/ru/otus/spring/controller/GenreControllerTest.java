package ru.otus.spring.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.dto.GenreDto;
import ru.otus.spring.service.GenreService;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.otus.spring.utils.Utils.getDbGenres;
import static ru.otus.spring.utils.Utils.getStringJsonByFilePath;

@WebMvcTest(GenreController.class)
class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreService genreService;

    @Test
    void getAllTest() throws Exception {
        given(genreService.findAll()).willReturn(getDbGenres().stream()
                .map(author -> GenreDto.builder()
                        .id(author.getId())
                        .name(author.getName())
                        .build())
                .toList());
        this.mockMvc
                .perform(get("/api/library/genres"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getStringJsonByFilePath("src/test/resources/json/allGenres.json")));
    }
}
