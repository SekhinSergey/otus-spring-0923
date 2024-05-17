package ru.otus.spring.config.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.model.mongo.UserDoc;

@Slf4j
@Configuration
public class UserWriteListener implements ItemWriteListener<UserDoc> {
    @Override
    public void afterWrite(Chunk<? extends UserDoc> items) {
        log.info("%d users migrated from JPA to Mongo".formatted(items.size()));
    }
}
