package ru.otus.spring.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

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

    @NotBlank(message = "field-value-should-not-be-blank")
    private String authorId;

    @NotBlank(message = "field-value-should-not-be-blank")
    private String genreIds;
}
