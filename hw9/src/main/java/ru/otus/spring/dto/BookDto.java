package ru.otus.spring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

/**
 * Валидация не позволит совершить попытку сохранения пустого значения, откатит назад, но и не выведет сообщение
 * Решить проблему не удалось
 */
@Getter
@Builder
public class BookDto {

    private Long id;

    @NotBlank(message = "field-value-should-not-be-blank")
    private String title;

    @NotNull(message = "field-value-should-not-be-null")
    private Long authorId;

    @NotEmpty(message = "field-value-should-not-be-empty")
    private Set<@NotNull(message = "field-value-should-not-be-null") Long> genreIds;
}
