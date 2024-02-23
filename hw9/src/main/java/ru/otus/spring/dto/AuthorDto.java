package ru.otus.spring.dto;

import lombok.Builder;

@Builder
public record AuthorDto(Long id, String fullName) {
}
