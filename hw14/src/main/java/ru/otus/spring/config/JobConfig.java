package ru.otus.spring.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static ru.otus.spring.util.Const.MIGRATION_NAME;

@Configuration
@RequiredArgsConstructor
public class JobConfig {

    private final JobRepository repository;

    private final ApplicationContext context;

    @Bean
    public Job migrateLibraryDatabase(Step authorMigrationStep,
                                      Step genreMigrationStep,
                                      Step bookMigrationStep,
                                      Step userMigrationStep,
                                      Step commentMigrationStep) {
        return new JobBuilder(MIGRATION_NAME, repository)
                .preventRestart()
                .start(authorMigrationStep)
                .next(genreMigrationStep)
                .next(bookMigrationStep)
                .next(userMigrationStep)
                .next(commentMigrationStep)
                // Cause limited by 5 args in checkstyle
                .next(getTokenMigrationStep())
                .build();
    }

    private Step getTokenMigrationStep() {
        return (Step) context.getBean("tokenMigrationStep");
    }
}
