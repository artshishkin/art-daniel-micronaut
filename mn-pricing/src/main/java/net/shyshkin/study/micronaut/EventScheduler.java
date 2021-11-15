package net.shyshkin.study.micronaut;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.scheduling.annotation.Scheduled;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.micronaut.quotes.external.ExternalQuote;
import net.shyshkin.study.micronaut.quotes.external.ExternalQuotesProducer;

import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Singleton
@Requires(notEnv = Environment.TEST)
@RequiredArgsConstructor
public class EventScheduler {

    private final static String[] SYMBOLS = {"APPL", "AMZN", "FB", "GOOG", "MSFT", "NFLX", "TSLA"};

    private final ExternalQuotesProducer producer;

    @Scheduled(fixedDelay = "1s")
    void generate() {

        String symbol = randomSymbol();
        ExternalQuote externalQuote = ExternalQuote.builder()
                .symbol(symbol)
                .volume(randomValue())
                .lastPrice(randomValue())
                .build();
        this.producer.send(symbol, externalQuote);
        log.debug("Generated {}", externalQuote);
    }

    private BigDecimal randomValue() {
        return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1, 100));
    }

    private String randomSymbol() {
        int index = ThreadLocalRandom.current().nextInt(SYMBOLS.length);
        return SYMBOLS[index];
    }

}
