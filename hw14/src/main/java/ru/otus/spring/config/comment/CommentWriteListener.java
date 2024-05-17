package ru.otus.spring.config.comment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.model.mongo.CommentDoc;

@Slf4j
@Configuration
public class CommentWriteListener implements ItemWriteListener<CommentDoc> {
    @Override
    public void afterWrite(Chunk<? extends CommentDoc> items) {
        log.info("%d comments migrated from JPA to Mongo".formatted(items.size()));
    }
}
