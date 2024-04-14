package ru.otus.spring.dto.create;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import static ru.otus.spring.utils.Messages.NO_BOOK_ID_ERROR_MESSAGE;
import static ru.otus.spring.utils.Messages.NO_COMMENT_TEXT_ERROR_MESSAGE;

@Getter
@Builder
public class CommentCreateDto {

    @NotBlank(message = NO_COMMENT_TEXT_ERROR_MESSAGE)
    private String text;

    @NotBlank(message = NO_BOOK_ID_ERROR_MESSAGE)
    private String bookId;
}
