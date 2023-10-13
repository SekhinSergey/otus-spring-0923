package ru.otus.spring.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Builder
@ToString
@EqualsAndHashCode
public class Question {
    private String text;

    private Answer studentAnswer;

    private Answer goodAnswer;

    private Answer isRight;
}
