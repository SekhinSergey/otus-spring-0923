package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.exception.EntityNotFoundException;
import ru.otus.spring.model.Author;
import ru.otus.spring.repository.AuthorRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public Author findById(String id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %s not found".formatted(id)));
    }

    @Override
    @Transactional
    public Author create(Author author) {
        return authorRepository.save(author);
    }

    @Override
    @Transactional
    public Author update(Author author) {
        findById(author.getId());
        return authorRepository.save(author);
    }

    @Override
    @Transactional
    public List<Author> createBatch(Set<Author> authors) {
        return authorRepository.saveAll(authors);
    }

    @Override
    @Transactional
    public List<Author> updateBatch(Set<Author> authors) {
        validateAuthors(authors);
        return authorRepository.saveAll(authors);
    }

    private void validateAuthors(Set<Author> authors) {
        Set<String> authorsIds = authors.stream()
                .map(Author::getId)
                .collect(Collectors.toCollection(HashSet::new));
        List<Author> foundAuthors = authorRepository.findAllById(authorsIds);
        if (authorsIds.size() != foundAuthors.size()) {
            throw new EntityNotFoundException(
                    "The number of requested authors does not match the number in the database");
        }
    }
}
