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

    @SuppressWarnings("all")
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

    @SuppressWarnings("all")
    @ShellMethod(value = "Find author by id", key = "abyid")
    public String findAuthorById(long id) {
        return authorConverter.authorToString(authorService.findById(id));
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Find author by full name", key = "abyf")
    public String findAuthorByFullName(String fullName) {
        return authorConverter.authorToString(authorService.findByFullName(fullName));
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Save and get author", key = "ains")
    public String saveAuthor(Author author) {
        return authorConverter.authorToString(authorService.save(author));
    }
}
