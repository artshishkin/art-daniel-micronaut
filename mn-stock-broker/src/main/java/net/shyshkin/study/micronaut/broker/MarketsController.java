package net.shyshkin.study.micronaut.broker;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import lombok.RequiredArgsConstructor;
import net.shyshkin.study.micronaut.store.InMemoryStore;

import java.util.List;

@Controller("/markets")
@RequiredArgsConstructor
public class MarketsController {

    private final InMemoryStore inMemoryStore;

    @Get
    public List<Symbol> all() {
        return inMemoryStore.getAllSymbols();
    }
}
