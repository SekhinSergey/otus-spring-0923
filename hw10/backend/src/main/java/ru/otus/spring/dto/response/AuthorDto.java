package ru.otus.spring.dto.response;

import lombok.Builder;

@Builder
public record AuthorDto(Long id, String fullName) {
}
