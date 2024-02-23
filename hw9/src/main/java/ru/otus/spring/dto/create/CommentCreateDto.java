package ru.otus.spring.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentCreateDto {

    @NotBlank(message = "field-value-should-not-be-blank")
    private String text;

    @NotNull(message = "field-value-should-not-be-null")
    private Long bookId;
}
