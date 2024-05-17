package ru.otus.spring.config.author;

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
import ru.otus.spring.model.jpa.Author;
import ru.otus.spring.model.mongo.AuthorDoc;

import static ru.otus.spring.util.Const.THREE_CHUNKS;

@Configuration
@RequiredArgsConstructor
public class AuthorMigrationConfig {

    private final JobRepository repository;

    private final PlatformTransactionManager manager;

    private final EntityManagerFactory factory;

    private final MongoTemplate template;

    private final AuthorMapper mapper;

    private final AuthorWriteListener listener;

    @Bean
    public ItemReader<Author> authorReader() {
        JpaPagingItemReader<Author> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(factory);
        reader.setQueryString("select a from Author a");
        return reader;
    }

    @Bean
    public ItemProcessor<Author, AuthorDoc> authorProcessor() {
        return mapper::toDoc;
    }

    @Bean
    public ItemWriter<AuthorDoc> authorWriter() {
        return new MongoItemWriterBuilder<AuthorDoc>()
                .collection("authors")
                .template(template)
                .build();
    }

    @Bean
    public Step authorMigrationStep() {
        return new StepBuilder("authorMigrationStep", repository)
                .<Author, AuthorDoc>chunk(THREE_CHUNKS, manager)
                .reader(authorReader())
                .processor(authorProcessor())
                .writer(authorWriter())
                .listener(listener)
                .build();
    }
}
