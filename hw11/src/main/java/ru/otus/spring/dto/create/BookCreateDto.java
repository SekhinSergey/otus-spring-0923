package ru.otus.spring.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

import static ru.otus.spring.utils.Messages.NO_AUTHOR_ID_ERROR_MESSAGE;
import static ru.otus.spring.utils.Messages.NO_BOOK_TITLE_ERROR_MESSAGE;
import static ru.otus.spring.utils.Messages.NO_GENRE_IDS_ERROR_MESSAGE;
import static ru.otus.spring.utils.Messages.NO_GENRE_ID_ERROR_MESSAGE;

@Getter
@Builder
public class BookCreateDto {

    @NotBlank(message = NO_BOOK_TITLE_ERROR_MESSAGE)
    private String title;

    @NotBlank(message = NO_AUTHOR_ID_ERROR_MESSAGE)
    private String authorId;

    @NotEmpty(message = NO_GENRE_IDS_ERROR_MESSAGE)
    private Set<@NotBlank(message = NO_GENRE_ID_ERROR_MESSAGE) String> genreIds;
}
