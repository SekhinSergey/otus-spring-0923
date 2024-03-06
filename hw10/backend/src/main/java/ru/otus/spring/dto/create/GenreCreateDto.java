package ru.otus.spring.dto.create;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GenreCreateDto {

    private Long id;

    @NotBlank(message = "Genre name value should not be blank")
    private String name;
}
