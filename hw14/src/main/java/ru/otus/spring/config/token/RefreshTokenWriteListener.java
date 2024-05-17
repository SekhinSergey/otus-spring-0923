package ru.otus.spring.config.token;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.model.mongo.RefreshTokenDoc;

@Slf4j
@Configuration
public class RefreshTokenWriteListener implements ItemWriteListener<RefreshTokenDoc> {
    @Override
    public void afterWrite(Chunk<? extends RefreshTokenDoc> items) {
        log.info("%d tokens migrated from JPA to Mongo".formatted(items.size()));
    }
}
