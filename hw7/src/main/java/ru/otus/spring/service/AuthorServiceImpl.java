package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.exception.EntityNotFoundException;
import ru.otus.spring.model.Author;
import ru.otus.spring.repository.AuthorRepository;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Author findById(long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public Author findByFullName(String fullName) {
        return authorRepository.findByFullName(fullName)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Author with full name %s not found".formatted(fullName)));
    }

    @Override
    @Transactional
    public Author save(Author author) {
        return authorRepository.save(author);
    }

    @Override
    @Transactional(readOnly = true)
    public Author findByExample(Author author) {
        return authorRepository.findOne(Example.of(author))
                .orElseThrow(() -> new EntityNotFoundException(
                        "Author with full name %s not found".formatted(author.getFullName())));
    }

    @Override
    @Transactional
    public List<Author> saveBatch(Set<Author> authors) {
        return authorRepository.saveAll(authors);
    }
}
