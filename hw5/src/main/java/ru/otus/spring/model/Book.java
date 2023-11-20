package ru.otus.spring.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class Book {

    private Long id;

    private String title;

    private Author author;

    private List<Genre> genres;
}
