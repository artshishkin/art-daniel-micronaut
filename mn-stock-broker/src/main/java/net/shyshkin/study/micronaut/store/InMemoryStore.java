package net.shyshkin.study.micronaut.store;

import net.shyshkin.study.micronaut.broker.Quote;
import net.shyshkin.study.micronaut.broker.Symbol;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class InMemoryStore {

    private List<Symbol> symbols;
    private final Map<String, Quote> cachedQuotes = new HashMap<>();
    private final ThreadLocalRandom current = ThreadLocalRandom.current();

    @PostConstruct
    void init() {
        this.symbols = Stream.of("APPL", "AMZN", "FB", "GOOG", "MSFT", "NFLX", "TSLA")
                .map(Symbol::new)
                .collect(Collectors.toList());

        this.symbols.forEach(symbol -> cachedQuotes.put(symbol.getValue(), randomQuote(symbol)));
    }

    public List<Symbol> getAllSymbols() {
        return symbols;
    }

    public Optional<Quote> fetchQuote(final String symbol) {
        return Optional.ofNullable(this.cachedQuotes.get(symbol));
    }

    public void updateQuote(final Quote quote) {
        this.cachedQuotes.put(quote.getSymbol().getValue(), quote);
    }

    private Quote randomQuote(Symbol symbol) {
        return Quote.builder()
                .symbol(symbol)
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
