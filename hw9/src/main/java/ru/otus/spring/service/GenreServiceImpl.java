package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dto.GenreDto;
import ru.otus.spring.dto.create.GenreCreateDto;
import ru.otus.spring.dto.update.GenreUpdateDto;
import ru.otus.spring.exception.NotFoundException;
import ru.otus.spring.mapper.GenreMapper;
import ru.otus.spring.model.Genre;
import ru.otus.spring.repository.GenreRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    private final GenreMapper genreMapper;

    @Override
    @Transactional(readOnly = true)
    public List<GenreDto> findAll() {
        return genreRepository.findAll().stream()
                .map(genreMapper::toDto)
                .toList();
    }

    @Override
    public GenreDto findById(Long id) {
        return genreMapper.toDto(genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Genre with id %d not found".formatted(id))));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GenreDto> findAllByIds(Set<Long> ids) {
        return genreRepository.findAllById(ids).stream()
                .map(genreMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public GenreCreateDto create(GenreCreateDto genreCreateDto) {
        throwExceptionIfExists(genreCreateDto);
        return genreMapper.toCreateDto(genreRepository.save(genreMapper.createDtoToEntity(genreCreateDto)));
    }

    @Override
    @Transactional
    public GenreUpdateDto update(GenreUpdateDto genreUpdateDto) {
        genreRepository.findById(genreUpdateDto.getId());
        return genreMapper.toUpdateDto(genreRepository.save(genreMapper.updateDtoToEntity(genreUpdateDto)));
    }

    @Override
    @Transactional
    public List<GenreCreateDto> createBatch(Set<GenreCreateDto> genreCreateDtos) {
        genreCreateDtos.forEach(this::throwExceptionIfExists);
        List<Genre> genres = genreCreateDtos.stream()
                .map(genreMapper::createDtoToEntity)
                .toList();
        return genreRepository.saveAll(genres).stream()
                .map(genreMapper::toCreateDto)
                .toList();
    }

    private void throwExceptionIfExists(GenreCreateDto genreCreateDto) {
        Long id = genreCreateDto.getId();
        if (genreRepository.findById(id).isPresent()) {
            throw new NotFoundException("Genre with id %d already exists".formatted(id));
        }
    }

    @Override
    @Transactional
    public List<GenreUpdateDto> updateBatch(Set<GenreUpdateDto> genreUpdateDtos) {
        Set<Long> genresIds = genreUpdateDtos.stream()
                .map(GenreUpdateDto::getId)
                .collect(Collectors.toCollection(HashSet::new));
        List<Genre> foundGenres = genreRepository.findAllById(genresIds);
        if (genresIds.size() != foundGenres.size()) {
            throw new NotFoundException(
                    "The number of requested genres does not match the number in the database");
        }
        List<Genre> genres = genreUpdateDtos.stream()
                .map(genreMapper::updateDtoToEntity)
                .toList();
        return genreRepository.saveAll(genres).stream()
                .map(genreMapper::toUpdateDto)
                .toList();
    }
}
