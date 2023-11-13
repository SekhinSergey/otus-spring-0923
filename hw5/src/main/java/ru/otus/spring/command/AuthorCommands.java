package ru.otus.spring.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.converter.AuthorConverter;
import ru.otus.spring.model.Author;
import ru.otus.spring.service.AuthorService;

import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class AuthorCommands {

    private final AuthorService authorService;

    private final AuthorConverter authorConverter;

    @ShellMethod(value = "Find all authors", key = "aa")
    public String findAllAuthors() {
        return authorService.findAll().stream()
                .map(authorConverter::authorToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find author by id", key = "abyid")
    public String findAuthorById(long id) {
        Author author = authorService.findById(id);
        return authorConverter.authorToString(author);
    }

    @ShellMethod(value = "Find author by full name", key = "abyf")
    public String findAuthorByFullName(String fullName) {
        Author author = authorService.findByFullName(fullName);
        return authorConverter.authorToString(author);
    }

    @ShellMethod(value = "Insert and get author", key = "ains")
    public Author insert(Author author) {
        return authorService.insert(author);
    }
}
