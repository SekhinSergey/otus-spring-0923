package ru.otus.spring.service;

import ru.otus.spring.dto.response.BookDto;
import ru.otus.spring.dto.create.BookCreateDto;
import ru.otus.spring.dto.update.BookUpdateDto;

import java.util.List;

public interface BookService {

    List<BookDto> findAll();

    BookDto create(BookCreateDto bookCreateDto);

    BookDto update(long id, BookUpdateDto bookUpdateDto);

    void deleteById(long id);
}
