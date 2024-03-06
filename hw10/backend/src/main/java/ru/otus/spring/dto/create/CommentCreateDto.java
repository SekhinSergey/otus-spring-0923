package ru.otus.spring.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentCreateDto {

    // Нельзя удалять, как минимум, потому что нам надо вернуть JSON с ID
    private Long id;

    @NotBlank(message = "Comment text value should not be blank")
    private String text;

    @NotNull(message = "Book ID value should not be null")
    private Long bookId;
}
