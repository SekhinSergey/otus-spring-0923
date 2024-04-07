package ru.otus.spring.mongock;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import com.mongodb.client.MongoDatabase;
import ru.otus.spring.model.Author;
import ru.otus.spring.model.Book;
import ru.otus.spring.model.Comment;
import ru.otus.spring.model.Genre;

import java.util.Set;

@ChangeLog
@SuppressWarnings("all")
public class Changelog {

    @ChangeSet(author = "SekhinSergey", id = "dropDatabase", order = "001", runAlways = true)
    public void dropDatabase(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(author = "SekhinSergey", id = "createData", order = "002", runAlways = true)
    public void createData(MongockTemplate mongoTemplate) {
        Author firstAuthor = new Author("1", "Author_1");
        Author secondAuthor = new Author("2", "Author_2");
        Author thirdAuthor = new Author("3", "Author_3");
        mongoTemplate.insertAll(Set.of(firstAuthor, secondAuthor, thirdAuthor));

        Genre firstGenre = new Genre("1", "Genre_1");
        Genre secondGenre = new Genre("2", "Genre_2");
        Genre thirdGenre = new Genre("3", "Genre_3");
        Genre fouthGenre = new Genre("4", "Genre_4");
        Genre fifthGenre = new Genre("5", "Genre_5");
        Genre sixthGenre = new Genre("6", "Genre_6");
        mongoTemplate.insertAll(Set.of(firstGenre, secondGenre, thirdGenre, fouthGenre, fifthGenre, sixthGenre));

        Book firstBook = new Book("1", "BookTitle_1", firstAuthor, Set.of(firstGenre, secondGenre));
        Book secondBook = new Book("2", "BookTitle_2", secondAuthor, Set.of(thirdGenre, fouthGenre));
        Book thirdBook = new Book("3", "BookTitle_3", thirdAuthor, Set.of(fifthGenre, sixthGenre));
        mongoTemplate.insertAll(Set.of(firstBook, secondBook, thirdBook));

        mongoTemplate.insertAll(Set.of(
                new Comment("1", "Comment_1", firstBook),
                new Comment("2", "Comment_2", secondBook),
                new Comment("3", "Comment_3", thirdBook)));
    }
}
