package ru.otus.spring.config.author;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.model.mongo.AuthorDoc;

@Slf4j
@Configuration
public class AuthorWriteListener implements ItemWriteListener<AuthorDoc> {
    @Override
    public void afterWrite(Chunk<? extends AuthorDoc> items) {
        log.info("%d authors migrated from JPA to Mongo".formatted(items.size()));
    }
}
