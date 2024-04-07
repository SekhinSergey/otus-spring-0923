package ru.otus.spring.dto.response;

import lombok.Builder;

@Builder
public record AuthorDto(String id, String fullName) {
}
