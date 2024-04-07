package ru.otus.spring.dto.response;

import lombok.Builder;

import java.util.Set;

@Builder
public record BookDto(String id, String title, String authorId, Set<String> genreIds) {
}
