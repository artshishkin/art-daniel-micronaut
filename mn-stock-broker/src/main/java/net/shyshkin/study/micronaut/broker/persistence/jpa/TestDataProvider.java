package net.shyshkin.study.micronaut.broker.persistence.jpa;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import lombok.RequiredArgsConstructor;
import net.shyshkin.study.micronaut.broker.persistence.model.SymbolEntity;

import javax.inject.Singleton;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
@Requires(notEnv = Environment.TEST)
@RequiredArgsConstructor
public class TestDataProvider {

    private final SymbolsRepository symbolsRepository;

    @EventListener
    void init(StartupEvent event) {

        if (symbolsRepository.count() == 0) {
            var symbols = Stream.of("APPL", "AMZN", "FB", "GOOG", "MSFT", "NFLX", "TSLA")
                    .map(SymbolEntity::new)
                    .collect(Collectors.toList());
            symbolsRepository.saveAll(symbols);
        }
    }

}
