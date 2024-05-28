package ru.otus.spring.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import ru.otus.spring.domain.Butterfly;
import ru.otus.spring.services.ButterflyService;

@Configuration
public class IntegrationConfig {

    @Bean
    public MessageChannelSpec<?, ?> caterpillarsChannel() {
        return MessageChannels.queue(5);
    }

    @Bean
    public MessageChannelSpec<?, ?> butterflyChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec poller() {
        return Pollers.fixedRate(1000).maxMessagesPerPoll(2);
    }

    @Bean
    public IntegrationFlow growFlow(ButterflyService butterflyService) {
        return IntegrationFlow.from(caterpillarsChannel())
                .split()
                .handle(butterflyService, "transform")
                .<Butterfly, Butterfly>transform(b -> new Butterfly(b.nickname().toUpperCase()))
                .aggregate()
                .channel(butterflyChannel())
                .get();
    }
}
