package ru.otus.spring.config.genre;

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
import ru.otus.spring.mapper.GenreMapper;
import ru.otus.spring.model.jpa.Genre;
import ru.otus.spring.model.mongo.GenreDoc;

import static ru.otus.spring.util.Const.SIX_CHUNKS;

@Configuration
@RequiredArgsConstructor
public class GenreMigrationConfig {

    private final JobRepository repository;

    private final PlatformTransactionManager manager;

    private final EntityManagerFactory factory;

    private final MongoTemplate template;

    private final GenreMapper mapper;

    private final GenreWriteListener listener;

    @Bean
    public ItemReader<Genre> genreReader() {
        JpaPagingItemReader<Genre> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(factory);
        reader.setQueryString("select g from Genre g");
        return reader;
    }

    @Bean
    public ItemProcessor<Genre, GenreDoc> genreProcessor() {
        return mapper::toDoc;
    }

    @Bean
    public ItemWriter<GenreDoc> genreWriter() {
        return new MongoItemWriterBuilder<GenreDoc>()
                .collection("genres")
                .template(template)
                .build();
    }

    @Bean
    public Step genreMigrationStep() {
        return new StepBuilder("genreMigrationStep", repository)
                .<Genre, GenreDoc>chunk(SIX_CHUNKS, manager)
                .reader(genreReader())
                .processor(genreProcessor())
                .writer(genreWriter())
                .listener(listener)
                .build();
    }
}
