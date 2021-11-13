package net.shyshkin.study.micronaut.broker;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.shyshkin.study.micronaut.broker.error.CustomError;
import net.shyshkin.study.micronaut.broker.persistence.jpa.QuotesRepository;
import net.shyshkin.study.micronaut.broker.persistence.model.QuoteEntity;
import net.shyshkin.study.micronaut.store.InMemoryStore;

import java.util.List;
import java.util.Optional;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/quotes")
@RequiredArgsConstructor
public class QuotesController {

    private final InMemoryStore store;
    private final QuotesRepository quotesRepository;

    @Operation(summary = "Returns a quote for a given symbol.")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @ApiResponse(responseCode = "404", description = "Invalid symbol specified")
    @Tag(name = "quotes")
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

    @Operation(summary = "Returns all the quotes. Fetched from the database via JPA.")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "quotes")
    @Get("/jpa")
    public List<QuoteEntity> getAllQuotesViaJpa() {
        return this.quotesRepository.findAll();
    }

}
