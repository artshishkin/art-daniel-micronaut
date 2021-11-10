package net.shyshkin.study.micronaut.store;

import net.shyshkin.study.micronaut.broker.Symbol;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class InMemoryStore {

    private List<Symbol> symbols;

    @PostConstruct
    void init() {
        this.symbols = Stream.of("AAPL", "AMZN", "FB", "GOOG", "MSFT", "NFLX", "TSLA")
                .map(Symbol::new)
                .collect(Collectors.toList());
    }

    public List<Symbol> getAllSymbols() {
        return symbols;
    }
}
