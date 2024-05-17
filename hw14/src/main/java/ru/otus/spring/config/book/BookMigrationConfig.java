package ru.otus.spring.config.book;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.spring.mapper.AuthorMapper;
import ru.otus.spring.mapper.BookMapper;
import ru.otus.spring.mapper.GenreMapper;
import ru.otus.spring.model.jpa.Book;
import ru.otus.spring.model.mongo.BookDoc;

import static ru.otus.spring.util.Const.THREE_CHUNKS;

@Configuration
@RequiredArgsConstructor
public class BookMigrationConfig {

    private final JobRepository repository;

    private final PlatformTransactionManager manager;

    private final EntityManagerFactory factory;

    private final MongoTemplate template;

    private final BookMapper bookMapper;

    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    private final BookWriteListener listener;

    @Bean
    public ItemReader<Book> bookReader() {
        JpaPagingItemReader<Book> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(factory);
        reader.setQueryString("select b from Book b");
        return reader;
    }

    @Bean
    public ItemProcessor<Book, BookDoc> bookProcessor() {
        return book -> {
            var mappedBook = bookMapper.toDoc(book);
            return BookDoc.builder()
                    .id(mappedBook.getId())
                    .title(mappedBook.getTitle())
                    .author(authorMapper.toDoc(book.getAuthor()))
                    .genres(book.getGenres().stream()
                            .map(genreMapper::toDoc)
                            .toList())
                    .build();
        };
    }

    @Bean
    public ItemWriter<BookDoc> bookWriter() {
        return new MongoItemWriterBuilder<BookDoc>()
                .collection("books")
                .template(template)
                .build();
    }

    @Bean
    public Step bookMigrationStep() {
        return new StepBuilder("bookMigrationStep", repository)
                .<Book, BookDoc>chunk(THREE_CHUNKS, manager)
                .reader(bookReader())
                .processor(bookProcessor())
                .writer(bookWriter())
                .listener(listener)
                .build();
    }
}
