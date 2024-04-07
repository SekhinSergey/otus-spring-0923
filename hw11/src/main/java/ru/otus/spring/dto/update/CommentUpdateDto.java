package ru.otus.spring.dto.update;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentUpdateDto {

    @NotBlank(message = "Comment text value should not be blank")
    private String text;

    @NotBlank(message = "Book ID value should not be null")
    private String bookId;
}
