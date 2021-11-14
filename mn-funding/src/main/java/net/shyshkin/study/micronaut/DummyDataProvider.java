package net.shyshkin.study.micronaut;

import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.scheduling.annotation.Scheduled;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class DummyDataProvider {

    private final static String[] SYMBOLS = {"APPL", "AMZN", "FB", "GOOG", "MSFT", "NFLX", "TSLA"};

    private final TransactionsRepository repository;

    @EventListener
    void init(StartupEvent event) {

        if (repository.count() == 0) {
            log.debug("No data were found in database - populating test data");
            IntStream.range(0, 50)
                    .mapToObj(i -> randomSymbol())
                    .map(this::transaction)
                    .forEach(repository::save);
        }
    }

    @Scheduled(fixedDelay = "1s")
    void generate() {
        Transaction savedTransaction = repository.save(this.transaction(randomSymbol()));
        log.debug("Transaction: {}", savedTransaction);
    }


    private Transaction transaction(String symbol) {
        return new Transaction(UUID.randomUUID(), symbol, randomValue());
    }

    private BigDecimal randomValue() {
        return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(-100, 100));
    }

    private String randomSymbol() {
        int index = ThreadLocalRandom.current().nextInt(SYMBOLS.length);
        return SYMBOLS[index];
    }

}
