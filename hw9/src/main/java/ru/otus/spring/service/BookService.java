package ru.otus.spring.service;

import ru.otus.spring.dto.BookDto;
import ru.otus.spring.dto.create.BookCreateDto;
import ru.otus.spring.dto.update.BookUpdateDto;

import java.util.List;
import java.util.Set;

@SuppressWarnings("all")
public interface BookService {

    List<BookDto> findAll();

    BookDto findById(Long id);

    BookUpdateDto findByIdForEditing(Long id);

    List<BookDto> findAllByIds(Set<Long> ids);

    BookCreateDto create(BookCreateDto bookCreateDto);

    BookUpdateDto update(BookUpdateDto bookUpdateDto);

    void deleteById(Long id);

    int countByAuthorId(long authorId);

    int countByGenreId(long genreId);

    List<BookCreateDto> createBatch(Set<BookCreateDto> bookCreateDtos);

    List<BookUpdateDto> updateBatch(Set<BookUpdateDto> bookUpdateDtos);

    void deleteAllByIds(Set<Long> ids);

    boolean isTest();
}
