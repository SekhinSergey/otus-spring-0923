package ru.otus.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.GenreService;

import java.util.Map;
import java.util.TreeMap;

@RestController
@RequiredArgsConstructor
public class AssociationController {

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/api/library/associations")
    public Map<String, Object> getAll() {
        Map<String, Object> map = new TreeMap<>();
        map.put("authors", authorService.findAll());
        map.put("genres", genreService.findAll());
        return map;
    }
}
