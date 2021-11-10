package net.shyshkin.study.micronaut.store;

import net.shyshkin.study.micronaut.broker.Quote;
import net.shyshkin.study.micronaut.broker.Symbol;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class InMemoryStore {

    private List<Symbol> symbols;
    private final ThreadLocalRandom current = ThreadLocalRandom.current();

    @PostConstruct
    void init() {
        this.symbols = Stream.of("AAPL", "AMZN", "FB", "GOOG", "MSFT", "NFLX", "TSLA")
                .map(Symbol::new)
                .collect(Collectors.toList());
    }

    public List<Symbol> getAllSymbols() {
        return symbols;
    }

    public Quote fetchQuote(final String symbol) {
        return Quote.builder()
                .symbol(new Symbol(symbol))
                .bid(randomValue())
                .ask(randomValue())
                .lastPrice(randomValue())
                .volume(randomValue())
                .build();
    }

    private BigDecimal randomValue() {
        return BigDecimal.valueOf(current.nextDouble(1, 100));
    }
}
