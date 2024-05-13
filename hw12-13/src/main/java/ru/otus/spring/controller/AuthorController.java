package ru.otus.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.dto.response.AuthorDto;
import ru.otus.spring.security.component.AccessService;
import ru.otus.spring.service.AuthorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    private final AccessService accessService;

    @GetMapping("/api/library/authors")
    public List<AuthorDto> getAll() {
        accessService.checkAccess();
        return authorService.findAll();
    }
}
