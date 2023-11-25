package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.exception.EntityNotFoundException;
import ru.otus.spring.model.Author;
import ru.otus.spring.repository.AuthorRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    public Author findById(long id) {
        return authorRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Author with id %d not found".formatted(id)));
    }

    public Author findByFullName(String fullName) {
        return authorRepository.findByFullName(fullName).orElseThrow(() ->
                new EntityNotFoundException("Author with full name %s not found".formatted(fullName)));
    }

    public Author insert(Author author) {
        return authorRepository.insert(author);
    }
}
