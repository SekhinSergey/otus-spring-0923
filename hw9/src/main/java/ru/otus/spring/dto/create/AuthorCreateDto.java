package ru.otus.spring.dto.create;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthorCreateDto {

    private Long id;

    @NotBlank(message = "field-value-should-not-be-blank")
    private String fullName;
}
