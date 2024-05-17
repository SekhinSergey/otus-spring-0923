package ru.otus.spring.config.token;

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
import ru.otus.spring.mapper.RefreshTokenMapper;
import ru.otus.spring.mapper.UserMapper;
import ru.otus.spring.model.jpa.RefreshToken;
import ru.otus.spring.model.mongo.RefreshTokenDoc;

import static ru.otus.spring.util.Const.THREE_CHUNKS;

@Configuration
@RequiredArgsConstructor
public class RefreshTokenMigrationConfig {

    private final JobRepository repository;

    private final PlatformTransactionManager manager;

    private final EntityManagerFactory factory;

    private final MongoTemplate template;

    private final RefreshTokenMapper tokenMapper;

    private final UserMapper userMapper;

    private final RefreshTokenWriteListener listener;

    @Bean
    public ItemReader<RefreshToken> tokenReader() {
        JpaPagingItemReader<RefreshToken> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(factory);
        reader.setQueryString("select t from RefreshToken t");
        return reader;
    }

    @Bean
    public ItemProcessor<RefreshToken, RefreshTokenDoc> tokenProcessor() {
        return refreshToken -> {
            var mappedToken = tokenMapper.toDoc(refreshToken);
            return RefreshTokenDoc.builder()
                    .id(mappedToken.getId())
                    .user(userMapper.toDoc(refreshToken.getUser()))
                    .token(mappedToken.getToken())
                    .revoked(mappedToken.getRevoked())
                    .build();
        };
    }

    @Bean
    public ItemWriter<RefreshTokenDoc> tokenWriter() {
        return new MongoItemWriterBuilder<RefreshTokenDoc>()
                .collection("tokens")
                .template(template)
                .build();
    }

    @Bean
    public Step tokenMigrationStep() {
        return new StepBuilder("tokenMigrationStep", repository)
                .<RefreshToken, RefreshTokenDoc>chunk(THREE_CHUNKS, manager)
                .reader(tokenReader())
                .processor(tokenProcessor())
                .writer(tokenWriter())
                .listener(listener)
                .build();
    }
}
