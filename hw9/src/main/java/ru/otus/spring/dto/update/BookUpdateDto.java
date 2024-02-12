package ru.otus.spring.dto.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookUpdateDto {

    @NotNull(message = "field-value-should-not-be-null")
    private Long id;

    @NotBlank(message = "field-value-should-not-be-blank")
    private String title;

    @NotNull(message = "field-value-should-not-be-null")
    private Long authorId;

    @NotBlank(message = "field-value-should-not-be-blank")
    private String genreIds;
}
