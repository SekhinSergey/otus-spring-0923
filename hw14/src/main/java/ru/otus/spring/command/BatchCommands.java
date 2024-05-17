package ru.otus.spring.command;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Properties;

import static ru.otus.spring.util.Const.MIGRATION_NAME;

@ShellComponent
@RequiredArgsConstructor
public class BatchCommands {

    private final JobOperator operator;

    @SuppressWarnings("unused")
    @ShellMethod(value = "startMigration", key = "sm")
    public void startMigration() throws JobExecutionException {
        operator.start(MIGRATION_NAME, new Properties());
    }
}
