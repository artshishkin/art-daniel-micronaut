package net.shyshkin.study.micronaut.broker;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import lombok.RequiredArgsConstructor;
import net.shyshkin.study.micronaut.broker.error.CustomError;
import net.shyshkin.study.micronaut.store.InMemoryStore;

import java.util.Optional;

@Controller("/quotes")
@RequiredArgsConstructor
public class QuotesController {

    private final InMemoryStore store;

    @Get(uri = "/{symbol}")
    public HttpResponse<?> quotes(@PathVariable(name = "symbol") String symbol) {

        final Optional<Quote> quoteOptional = this.store.fetchQuote(symbol);
        if (quoteOptional.isEmpty()) {
            CustomError customError = CustomError.builder()
                    .status(HttpStatus.NOT_FOUND.getCode())
                    .error(HttpStatus.NOT_FOUND.name())
                    .message("quote for symbol not available")
                    .path("/quotes/" + symbol)
                    .build();
            return HttpResponse.notFound(customError);
        }
        return HttpResponse.ok(quoteOptional.get());
    }

}
