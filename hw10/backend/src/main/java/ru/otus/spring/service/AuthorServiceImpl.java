package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dto.response.AuthorDto;
import ru.otus.spring.dto.create.AuthorCreateDto;
import ru.otus.spring.dto.update.AuthorUpdateDto;
import ru.otus.spring.exception.NotFoundException;
import ru.otus.spring.mapper.AuthorMapper;
import ru.otus.spring.model.Author;
import ru.otus.spring.repository.AuthorRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toCollection;
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
    public AuthorDto findById(long id) {
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
    public AuthorDto create(AuthorCreateDto authorCreateDto) {
        return authorMapper.toDto(authorRepository.save(authorMapper.createDtoToEntity(authorCreateDto)));
    }

    @Override
    @Transactional
    public AuthorDto update(AuthorUpdateDto authorUpdateDto) {
        Long id = authorUpdateDto.getId();
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NO_AUTHOR_BY_ID_ERROR_MESSAGE.formatted(id)));
        String fullName = authorUpdateDto.getFullName();
        if (author.getFullName().equals(fullName)) {
            return authorMapper.toDto(author);
        } else {
            author.setFullName(fullName);
        }
        return authorMapper.toDto(authorRepository.save(author));
    }

    @Override
    @Transactional
    public List<AuthorDto> createBatch(Set<AuthorCreateDto> authorCreateDtos) {
        List<Author> authors = authorCreateDtos.stream()
                .map(authorMapper::createDtoToEntity)
                .toList();
        return authorRepository.saveAll(authors).stream()
                .map(authorMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public List<AuthorDto> updateBatch(Set<AuthorUpdateDto> authorUpdateDtos) {
        Set<Long> authorsIds = authorUpdateDtos.stream()
                .map(AuthorUpdateDto::getId)
                .collect(toCollection(HashSet::new));
        List<Author> foundAuthors = authorRepository.findAllById(authorsIds);
        if (authorsIds.size() != foundAuthors.size()) {
            throw new NotFoundException(AUTHORS_SIZE_ERROR_MESSAGE);
        }
        List<Author> authors = authorUpdateDtos.stream()
                .map(authorMapper::updateDtoToEntity)
                .toList();
        return authorRepository.saveAll(authors).stream()
                .map(authorMapper::toDto)
                .toList();
    }
}
