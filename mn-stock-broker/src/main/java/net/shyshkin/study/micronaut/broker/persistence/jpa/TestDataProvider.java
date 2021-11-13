package net.shyshkin.study.micronaut.broker.persistence.jpa;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import lombok.RequiredArgsConstructor;
import net.shyshkin.study.micronaut.broker.persistence.model.QuoteEntity;
import net.shyshkin.study.micronaut.broker.persistence.model.SymbolEntity;

import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
@Requires(notEnv = Environment.TEST)
@RequiredArgsConstructor
public class TestDataProvider {

    private final SymbolsRepository symbolsRepository;
    private final QuotesRepository quotesRepository;

    @EventListener
    void init(StartupEvent event) {

        if (symbolsRepository.count() == 0) {
            var symbols = Stream.of("APPL", "AMZN", "FB", "GOOG", "MSFT", "NFLX", "TSLA")
                    .map(SymbolEntity::new)
                    .collect(Collectors.toList());
            symbolsRepository.saveAll(symbols);

            symbols.stream()
                    .map(this::initRandomQuote)
                    .forEach(quotesRepository::save);
        }
    }

    private QuoteEntity initRandomQuote(SymbolEntity symbol) {
        return QuoteEntity.builder()
                .symbol(symbol)
                .bid(randomValue())
                .ask(randomValue())
                .lastPrice(randomValue())
                .volume(randomValue())
                .build();
    }

    private BigDecimal randomValue() {
        return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1, 100));
    }


}
