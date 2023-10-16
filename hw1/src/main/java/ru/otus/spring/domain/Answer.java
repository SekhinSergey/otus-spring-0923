package ru.otus.spring.domain;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Answer {

    private String text;

    private Boolean isRight;

    public Answer(String text) {
        this.text = text;
    }

    public Answer(boolean isRight) {
        this.isRight = isRight;
    }

    @Override
    public String toString() {
        return text == null ? isRight.toString() : text;
    }
}
