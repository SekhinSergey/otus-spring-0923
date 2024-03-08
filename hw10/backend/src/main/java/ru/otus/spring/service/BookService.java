package ru.otus.spring.service;

import ru.otus.spring.dto.response.BookDto;
import ru.otus.spring.dto.create.BookCreateDto;
import ru.otus.spring.dto.update.BookUpdateDto;

import java.util.List;
import java.util.Set;

public interface BookService {

    List<BookDto> findAll();

    BookDto findById(long id);

    List<BookDto> findAllByIds(Set<Long> ids);

    BookDto create(BookCreateDto bookCreateDto);

    BookDto update(BookUpdateDto bookUpdateDto);

    void deleteById(long id);

    int countByAuthorId(long authorId);

    int countByGenreId(long genreId);

    List<BookDto> updateBatch(Set<BookUpdateDto> bookUpdateDtos);

    void deleteAllByIds(Set<Long> ids);
}
