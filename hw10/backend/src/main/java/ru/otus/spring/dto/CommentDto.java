package ru.otus.spring.dto;

import lombok.Builder;

@Builder
public record CommentDto(Long id, String text, Long bookId) {
}
