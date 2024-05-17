package ru.otus.spring.config.book;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.model.mongo.BookDoc;

@Slf4j
@Configuration
public class BookWriteListener implements ItemWriteListener<BookDoc> {
    @Override
    public void afterWrite(Chunk<? extends BookDoc> items) {
        log.info("%d books migrated from JPA to Mongo".formatted(items.size()));
    }
}
