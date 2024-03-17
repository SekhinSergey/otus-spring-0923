package ru.otus.spring.dto.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Builder
public class BookUpdateDto {

    @Setter
    private Long id;

    @NotBlank(message = "Book title value should not be blank")
    private String title;

    @NotNull(message = "Author ID value should not be null")
    private Long authorId;

    @NotEmpty(message = "Set of genre IDs value should not be empty")
    private Set<@NotNull(message = "Genre ID value should not be null") Long> genreIds;
}
