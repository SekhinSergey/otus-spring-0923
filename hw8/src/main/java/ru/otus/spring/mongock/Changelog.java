package ru.otus.spring.mongock;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.spring.model.Author;
import ru.otus.spring.model.Book;
import ru.otus.spring.model.Comment;
import ru.otus.spring.model.Genre;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.CommentRepository;
import ru.otus.spring.repository.GenreRepository;

import java.util.Arrays;

@ChangeLog
@SuppressWarnings("all")
public class Changelog {

    @ChangeSet(author = "SekhinSergey", id = "dropDatabase", order = "001", runAlways = true)
    public void dropDatabase(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(author = "SekhinSergey", id = "createData", order = "002", runAlways = true)
    public void createData(AuthorRepository authorRepository,
                           BookRepository bookRepository,
                           GenreRepository genreRepository,
                           CommentRepository commentRepository) {
        Author firstAuthor = new Author("1", "Author_1");
        Author secondAuthor = new Author("2", "Author_2");
        Author thirdAuthor = new Author("3", "Author_3");
        authorRepository.saveAll(Arrays.asList(firstAuthor, secondAuthor, thirdAuthor));

        Genre firstGenre = new Genre("1", "Genre_1");
        Genre secondGenre = new Genre("2", "Genre_2");
        Genre thirdGenre = new Genre("3", "Genre_3");
        Genre fouthGenre = new Genre("4", "Genre_4");
        Genre fifthGenre = new Genre("5", "Genre_5");
        Genre sixthGenre = new Genre("6", "Genre_6");
        genreRepository.saveAll(Arrays.asList(firstGenre, secondGenre, thirdGenre, fouthGenre, fifthGenre, sixthGenre));

        Book firstBook = new Book("1", "BookTitle_1", firstAuthor, Arrays.asList(firstGenre, secondGenre));
        Book secondBook = new Book("2", "BookTitle_2", secondAuthor, Arrays.asList(thirdGenre, fouthGenre));
        Book thirdBook = new Book("3", "BookTitle_3", thirdAuthor, Arrays.asList(fifthGenre, sixthGenre));
        bookRepository.saveAll(Arrays.asList(firstBook, secondBook, thirdBook));

        commentRepository.saveAll(Arrays.asList(
                new Comment("1", "Comment_1", firstBook),
                new Comment("2", "Comment_2", secondBook),
                new Comment("3", "Comment_3", thirdBook)));
    }
}
