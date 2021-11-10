package net.shyshkin.study.micronaut.broker;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import lombok.RequiredArgsConstructor;
import net.shyshkin.study.micronaut.store.InMemoryStore;

@Controller("/quotes")
@RequiredArgsConstructor
public class QuotesController {

    private final InMemoryStore store;

    @Get(uri = "/{symbol}")
    public HttpResponse<Quote> quotes(@PathVariable(name = "symbol") String symbol) {
        var quote = this.store.fetchQuote(symbol);
        return HttpResponse.ok(quote);
    }

}
