package ru.otus.spring.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.converter.AuthorConverter;
import ru.otus.spring.model.Author;
import ru.otus.spring.service.AuthorService;

import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class AuthorCommands {

    private final AuthorService authorService;

    private final AuthorConverter authorConverter;

    @ShellMethod(value = "Find all authors", key = "aa")
    public String findAllAuthors() {
        List<Author> authors = authorService.findAll();
        if (authors.isEmpty()) {
            return "No authors found";
        }
        return authors.stream()
                .map(authorConverter::authorToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find author by id", key = "abyid")
    public String findAuthorById(long id) {
        return authorConverter.authorToString(authorService.findById(id));
    }

    @ShellMethod(value = "Find author by full name", key = "abyf")
    public String findAuthorByFullName(String fullName) {
        return authorConverter.authorToString(authorService.findByFullName(fullName));
    }

    @ShellMethod(value = "Insert and get author", key = "ains")
    public String insert(Author author) {
        return authorConverter.authorToString(authorService.insert(author));
    }
}
