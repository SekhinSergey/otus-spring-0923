package ru.otus.spring.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.otus.spring.services.ButterflyGateway;
import ru.otus.spring.domain.Butterfly;
import ru.otus.spring.domain.Caterpillar;

import java.util.Collection;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationRunner implements CommandLineRunner {

    private final ButterflyGateway butterflyGateway;

    @Override
    public void run(String... args)  {
        log.info("Start integration");
        List<Caterpillar> caterpillars = List.of(
                new Caterpillar("Гусеница 1"),
                new Caterpillar("Гусеница 2"),
                new Caterpillar("Гусеница 3"),
                new Caterpillar("Гусеница 4")
        );
        Collection<Butterfly> result = butterflyGateway.process(caterpillars);
        log.info("Transformation from caterpillars to butterflies has finished");
        log.info(result.toString());
    }
}
