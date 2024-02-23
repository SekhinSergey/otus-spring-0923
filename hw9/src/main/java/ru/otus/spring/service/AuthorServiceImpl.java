package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.dto.create.AuthorCreateDto;
import ru.otus.spring.dto.update.AuthorUpdateDto;
import ru.otus.spring.exception.NotFoundException;
import ru.otus.spring.mapper.AuthorMapper;
import ru.otus.spring.model.Author;
import ru.otus.spring.repository.AuthorRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.otus.spring.constant.Constants.AUTHORS_SIZE_ERROR_MESSAGE;
import static ru.otus.spring.constant.Constants.NO_AUTHOR_BY_ID_ERROR_MESSAGE;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    @Override
    @Transactional(readOnly = true)
    public List<AuthorDto> findAll() {
        return authorRepository.findAll().stream()
                .map(authorMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorDto findById(Long id) {
        return authorMapper.toDto(authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NO_AUTHOR_BY_ID_ERROR_MESSAGE.formatted(id))));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorDto> findAllByIds(Set<Long> ids) {
        return authorRepository.findAllById(ids).stream()
                .map(authorMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public AuthorCreateDto create(AuthorCreateDto authorCreateDto) {
        long id = authorCreateDto.getId();
        if (authorRepository.findById(id).isPresent()) {
            throw new NotFoundException("Author with id %d already exists".formatted(id));
        }
        return authorMapper.toCreateDto(authorRepository.save(authorMapper.createDtoToEntity(authorCreateDto)));
    }

    @Override
    @Transactional
    public AuthorUpdateDto update(AuthorUpdateDto authorUpdateDto) {
        Long id = authorUpdateDto.getId();
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NO_AUTHOR_BY_ID_ERROR_MESSAGE.formatted(id)));
        String fullName = authorUpdateDto.getFullName();
        if (author.getFullName().equals(fullName)) {
            return authorMapper.toUpdateDto(author);
        } else {
            author.setFullName(fullName);
        }
        return authorMapper.toUpdateDto(authorRepository.save(author));
    }

    @Override
    @Transactional
    public List<AuthorCreateDto> createBatch(Set<AuthorCreateDto> authorCreateDtos) {
        Set<Long> ids = authorCreateDtos.stream()
                .map(AuthorCreateDto::getId)
                .collect(Collectors.toSet());
        if (!authorRepository.findAllById(ids).isEmpty()) {
            throw new NotFoundException("Some authors already exists");
        }
        List<Author> authors = authorCreateDtos.stream()
                .map(authorMapper::createDtoToEntity)
                .toList();
        return authorRepository.saveAll(authors).stream()
                .map(authorMapper::toCreateDto)
                .toList();
    }

    @Override
    @Transactional
    public List<AuthorUpdateDto> updateBatch(Set<AuthorUpdateDto> authorUpdateDtos) {
        Set<Long> authorsIds = authorUpdateDtos.stream()
                .map(AuthorUpdateDto::getId)
                .collect(Collectors.toCollection(HashSet::new));
        List<Author> foundAuthors = authorRepository.findAllById(authorsIds);
        if (authorsIds.size() != foundAuthors.size()) {
            throw new NotFoundException(AUTHORS_SIZE_ERROR_MESSAGE);
        }
        List<Author> authors = authorUpdateDtos.stream()
                .map(authorMapper::updateDtoToEntity)
                .toList();
        return authorRepository.saveAll(authors).stream()
                .map(authorMapper::toUpdateDto)
                .toList();
    }
}
