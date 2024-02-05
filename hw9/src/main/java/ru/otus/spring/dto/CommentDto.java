package ru.otus.spring.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentDto {

    private Long id;

    /**
     * Валидация не позволит совершить попытку сохранения пустого значения, откатит назад, но и не выведет сообщение
     * Решить проблему не удалось
     * Так же индикатором проблемы является то, что бандл помечает переменную как неиспользуемую
     */
    @NotBlank(message = "{field-value-should-not-be-blank}")
    private String text;

    private Long bookId;
}
