package ru.otus.spring.config.comment;

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
import ru.otus.spring.mapper.BookMapper;
import ru.otus.spring.mapper.CommentMapper;
import ru.otus.spring.mapper.UserMapper;
import ru.otus.spring.model.jpa.Comment;
import ru.otus.spring.model.mongo.CommentDoc;

import static ru.otus.spring.util.Const.THREE_CHUNKS;

@Configuration
@RequiredArgsConstructor
public class CommentMigrationConfig {

    private final JobRepository repository;

    private final PlatformTransactionManager manager;

    private final EntityManagerFactory factory;

    private final MongoTemplate template;

    private final CommentMapper commentMapper;

    private final BookMapper bookMapper;

    private final UserMapper userMapper;

    private final CommentWriteListener listener;

    @Bean
    public ItemReader<Comment> commentReader() {
        JpaPagingItemReader<Comment> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(factory);
        reader.setQueryString("select a from Comment a");
        return reader;
    }

    @Bean
    public ItemProcessor<Comment, CommentDoc> commentProcessor() {
        return comment -> {
            var mappedComment = commentMapper.toDoc(comment);
            return CommentDoc.builder()
                    .id(mappedComment.getId())
                    .text(mappedComment.getText())
                    .book(bookMapper.toDoc(comment.getBook()))
                    .user(userMapper.toDoc(comment.getUser()))
                    .build();
        };
    }

    @Bean
    public ItemWriter<CommentDoc> commentWriter() {
        return new MongoItemWriterBuilder<CommentDoc>()
                .collection("comments")
                .template(template)
                .build();
    }

    @Bean
    public Step commentMigrationStep() {
        return new StepBuilder("commentMigrationStep", repository)
                .<Comment, CommentDoc>chunk(THREE_CHUNKS, manager)
                .reader(commentReader())
                .processor(commentProcessor())
                .writer(commentWriter())
                .listener(listener)
                .build();
    }
}
