package ru.otus.spring.dto.create;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentCreateDto {

    @NotBlank(message = "Comment text value should not be blank")
    private String text;

    @NotBlank(message = "Book ID value should not be null")
    private String bookId;
}
