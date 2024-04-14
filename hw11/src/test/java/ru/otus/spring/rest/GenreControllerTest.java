package ru.otus.spring.rest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import ru.otus.spring.dto.response.GenreDto;
import ru.otus.spring.repository.GenreRepository;
import ru.otus.spring.utils.IntegrationTest;

import static org.mockito.Mockito.when;
import static ru.otus.spring.utils.EntityUtils.FIFTH_GENRE_NAME;
import static ru.otus.spring.utils.EntityUtils.FIFTH_ID;
import static ru.otus.spring.utils.EntityUtils.FIRST_GENRE_NAME;
import static ru.otus.spring.utils.EntityUtils.FIRST_ID;
import static ru.otus.spring.utils.EntityUtils.FOURTH_GENRE_NAME;
import static ru.otus.spring.utils.EntityUtils.FOURTH_ID;
import static ru.otus.spring.utils.EntityUtils.SECOND_GENRE_NAME;
import static ru.otus.spring.utils.EntityUtils.SECOND_ID;
import static ru.otus.spring.utils.EntityUtils.SIXTH_GENRE_NAME;
import static ru.otus.spring.utils.EntityUtils.SIXTH_ID;
import static ru.otus.spring.utils.EntityUtils.THIRD_GENRE_NAME;
import static ru.otus.spring.utils.EntityUtils.THIRD_ID;
import static ru.otus.spring.utils.EntityUtils.getFifthGenre;
import static ru.otus.spring.utils.EntityUtils.getFirstGenre;
import static ru.otus.spring.utils.EntityUtils.getFourthGenre;
import static ru.otus.spring.utils.EntityUtils.getSecondGenre;
import static ru.otus.spring.utils.EntityUtils.getSixthGenre;
import static ru.otus.spring.utils.EntityUtils.getThirdGenre;

class GenreControllerTest extends IntegrationTest {

    @MockBean
    private GenreRepository repository;


    @Test
    void shouldGetAllAuthors() {
        when(repository.findAll()).thenReturn(Flux.just(
                getFirstGenre(),
                getSecondGenre(),
                getThirdGenre(),
                getFourthGenre(),
                getFifthGenre(),
                getSixthGenre()));
        webTestClientBuild
                .get()
                .uri("/api/library/genres")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GenreDto.class)
                .hasSize(6)
                .contains(
                        GenreDto.builder()
                                .id(FIRST_ID)
                                .name(FIRST_GENRE_NAME)
                                .build(),
                        GenreDto.builder()
                                .id(SECOND_ID)
                                .name(SECOND_GENRE_NAME)
                                .build(),
                        GenreDto.builder()
                                .id(THIRD_ID)
                                .name(THIRD_GENRE_NAME)
                                .build(),
                        GenreDto.builder()
                                .id(FOURTH_ID)
                                .name(FOURTH_GENRE_NAME)
                                .build(),
                        GenreDto.builder()
                                .id(FIFTH_ID)
                                .name(FIFTH_GENRE_NAME)
                                .build(),
                        GenreDto.builder()
                                .id(SIXTH_ID)
                                .name(SIXTH_GENRE_NAME)
                                .build());
    }
}
