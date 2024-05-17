package ru.otus.spring;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.spring.model.mongo.AuthorDoc;
import ru.otus.spring.model.mongo.BookDoc;
import ru.otus.spring.model.mongo.CommentDoc;
import ru.otus.spring.model.mongo.GenreDoc;
import ru.otus.spring.model.mongo.RefreshTokenDoc;
import ru.otus.spring.model.mongo.UserDoc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.otus.spring.util.Const.MIGRATION_NAME;
import static ru.otus.spring.util.Const.SIX_CHUNKS;
import static ru.otus.spring.util.Const.THREE_CHUNKS;

@SpringBootTest
@SpringBatchTest
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
class MigrationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void migrationTest() throws Exception {
        Assertions
                .assertThat(jobLauncherTestUtils.getJob())
                .isNotNull()
                .extracting(Job::getName)
                .isEqualTo(MIGRATION_NAME);
        assertThat(jobLauncherTestUtils
                .launchJob()
                .getExitStatus()
                .getExitCode())
                .isEqualTo("COMPLETED");
        assertEquals(THREE_CHUNKS, mongoTemplate.count(new Query(), AuthorDoc.class));
        assertEquals(THREE_CHUNKS, mongoTemplate.count(new Query(), BookDoc.class));
        assertEquals(THREE_CHUNKS, mongoTemplate.count(new Query(), CommentDoc.class));
        assertEquals(SIX_CHUNKS, mongoTemplate.count(new Query(), GenreDoc.class));
        assertEquals(THREE_CHUNKS, mongoTemplate.count(new Query(), RefreshTokenDoc.class));
        assertEquals(THREE_CHUNKS, mongoTemplate.count(new Query(), UserDoc.class));
    }
}
