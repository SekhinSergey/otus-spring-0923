package ru.otus.spring.config.user;

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
import ru.otus.spring.mapper.UserMapper;
import ru.otus.spring.model.jpa.User;
import ru.otus.spring.model.mongo.UserDoc;

@Configuration
@RequiredArgsConstructor
public class UserMigrationConfig {

    private final JobRepository repository;

    private final PlatformTransactionManager manager;

    private final EntityManagerFactory factory;

    private final MongoTemplate template;

    private final UserMapper mapper;

    private final UserWriteListener listener;

    @Bean
    public ItemReader<User> userReader() {
        JpaPagingItemReader<User> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(factory);
        reader.setQueryString("select u from User u");
        return reader;
    }

    @Bean
    public ItemProcessor<User, UserDoc> userProcessor() {
        return mapper::toDoc;
    }

    @Bean
    public ItemWriter<UserDoc> userWriter() {
        return new MongoItemWriterBuilder<UserDoc>()
                .collection("users")
                .template(template)
                .build();
    }

    @Bean
    public Step userMigrationStep() {
        return new StepBuilder("userMigrationStep", repository)
                .<User, UserDoc>chunk(3, manager)
                .reader(userReader())
                .processor(userProcessor())
                .writer(userWriter())
                .listener(listener)
                .build();
    }
}
