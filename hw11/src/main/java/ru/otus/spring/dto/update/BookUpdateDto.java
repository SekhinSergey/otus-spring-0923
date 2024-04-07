package ru.otus.spring.dto.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class BookUpdateDto {

    @NotBlank(message = "Book title value should not be blank")
    private String title;

    @NotBlank(message = "Author ID value should not be null")
    private String authorId;

    @NotEmpty(message = "Set of genre IDs value should not be empty")
    private Set<@NotBlank(message = "Genre ID value should not be null") String> genreIds;
}
