package ru.otus.spring.rest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import ru.otus.spring.dto.response.AuthorDto;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.utils.IntegrationTest;

import static org.mockito.Mockito.when;
import static ru.otus.spring.utils.EntityUtils.FIRST_AUTHOR_NAME;
import static ru.otus.spring.utils.EntityUtils.FIRST_ID;
import static ru.otus.spring.utils.EntityUtils.SECOND_AUTHOR_NAME;
import static ru.otus.spring.utils.EntityUtils.SECOND_ID;
import static ru.otus.spring.utils.EntityUtils.THIRD_AUTHOR_NAME;
import static ru.otus.spring.utils.EntityUtils.THIRD_ID;
import static ru.otus.spring.utils.EntityUtils.getFirstAuthor;
import static ru.otus.spring.utils.EntityUtils.getSecondAuthor;
import static ru.otus.spring.utils.EntityUtils.getThirdAuthor;

class AuthorControllerTest extends IntegrationTest {

    @MockBean
    private AuthorRepository repository;

    @Test
    void shouldGetAllAuthors() {
        when(repository.findAll()).thenReturn(Flux.just(getFirstAuthor(), getSecondAuthor(), getThirdAuthor()));
        webTestClientBuild
                .get()
                .uri("/api/library/authors")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AuthorDto.class)
                .hasSize(3)
                .contains(
                        AuthorDto.builder()
                                .id(FIRST_ID)
                                .fullName(FIRST_AUTHOR_NAME)
                                .build(),
                        AuthorDto.builder()
                                .id(SECOND_ID)
                                .fullName(SECOND_AUTHOR_NAME)
                                .build(),
                        AuthorDto.builder()
                                .id(THIRD_ID)
                                .fullName(THIRD_AUTHOR_NAME)
                                .build());
    }
}
