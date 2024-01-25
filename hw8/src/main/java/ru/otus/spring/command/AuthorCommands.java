package ru.otus.spring.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.converter.AuthorConverter;
import ru.otus.spring.model.Author;
import ru.otus.spring.service.AuthorService;

import java.util.List;
import java.util.Set;
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
    public String findAuthorById(String id) {
        return authorConverter.authorToString(authorService.findById(id));
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Create and get author", key = "anew")
    public String createAuthor(Author author) {
        return authorConverter.authorToString(authorService.create(author));
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Update and get author", key = "aupd")
    public String updateAuthor(Author author) {
        return authorConverter.authorToString(authorService.update(author));
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Create author batch", key = "anewb")
    public String createAuthorBatch(Set<Author> authors) {
        return authorService.createBatch(authors).stream()
                .map(authorConverter::authorToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @SuppressWarnings("all")
    @ShellMethod(value = "Update author batch", key = "aupdb")
    public String updateAuthorBatch(Set<Author> authors) {
        return authorService.updateBatch(authors).stream()
                .map(authorConverter::authorToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }
}
