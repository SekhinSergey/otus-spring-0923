package ru.otus.spring.dto.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import static ru.otus.spring.constant.Constants.NO_BOOK_ID_ERROR_MESSAGE;
import static ru.otus.spring.constant.Constants.NO_COMMENT_TEXT_ERROR_MESSAGE;
import static ru.otus.spring.constant.Constants.NO_USER_ID_ERROR_MESSAGE;

@Builder
public record CommentUpdateDto(Long id,
                               @NotBlank(message = NO_COMMENT_TEXT_ERROR_MESSAGE) String text,
                               @NotNull(message = NO_BOOK_ID_ERROR_MESSAGE) Long bookId,
                               @NotNull(message = NO_USER_ID_ERROR_MESSAGE) Long userId) {


}
