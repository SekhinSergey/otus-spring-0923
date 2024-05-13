package ru.otus.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.dto.response.GenreDto;
import ru.otus.spring.security.component.AccessService;
import ru.otus.spring.service.GenreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    private final AccessService accessService;

    @GetMapping("/api/library/genres")
    public List<GenreDto> getAll() {
        accessService.checkAccess();
        return genreService.findAll();
    }
}
