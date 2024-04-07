package ru.otus.spring.dto.response;

import lombok.Builder;

@Builder
public record CommentDto(String id, String text, String bookId) {
}
