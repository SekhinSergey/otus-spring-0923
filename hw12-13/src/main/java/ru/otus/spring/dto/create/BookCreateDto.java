package ru.otus.spring.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.Set;

import static ru.otus.spring.constant.Constants.NO_AUTHOR_ID_ERROR_MESSAGE;
import static ru.otus.spring.constant.Constants.NO_BOOK_TITLE_ERROR_MESSAGE;
import static ru.otus.spring.constant.Constants.NO_GENRE_IDS_ERROR_MESSAGE;
import static ru.otus.spring.constant.Constants.NO_GENRE_ID_ERROR_MESSAGE;

@Builder
public record BookCreateDto(@NotBlank(message = NO_BOOK_TITLE_ERROR_MESSAGE) String title,

                            @NotNull(message = NO_AUTHOR_ID_ERROR_MESSAGE) Long authorId,

                            @NotEmpty(message = NO_GENRE_IDS_ERROR_MESSAGE)
                            Set<@NotNull(message = NO_GENRE_ID_ERROR_MESSAGE) Long> genreIds) {
}
