package net.shyshkin.study.micronaut.broker;

import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.Slice;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.micronaut.broker.error.CustomError;
import net.shyshkin.study.micronaut.broker.persistence.jpa.QuotesRepository;
import net.shyshkin.study.micronaut.broker.persistence.model.QuoteDto;
import net.shyshkin.study.micronaut.broker.persistence.model.QuoteEntity;
import net.shyshkin.study.micronaut.broker.persistence.model.SymbolEntity;
import net.shyshkin.study.micronaut.store.InMemoryStore;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
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

    @Operation(summary = "Returns a quote for a given symbol. Fetched from the database via JPA.")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @ApiResponse(responseCode = "404", description = "Invalid symbol specified")
    @Tag(name = "quotes")
    @Get(uri = "/jpa/{symbol}")
    public HttpResponse<?> getQuoteViaJpa(@PathVariable(name = "symbol") String symbol) {

        final List<QuoteEntity> quotes = this.quotesRepository.findAllBySymbolValue(symbol);
        if (quotes.isEmpty()) {
            CustomError customError = CustomError.builder()
                    .status(HttpStatus.NOT_FOUND.getCode())
                    .error(HttpStatus.NOT_FOUND.name())
                    .message("quote for symbol not available in db")
                    .path("/quotes/jpa/" + symbol)
                    .build();
            return HttpResponse.notFound(customError);
        }
        return HttpResponse.ok(quotes);
    }

    @Operation(summary = "Returns a quote for a given symbol. Fetched from the database via JPA. Using SymbolEntity")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @ApiResponse(responseCode = "404", description = "Invalid symbol specified")
    @Tag(name = "quotes")
    @Get(uri = "/jpa/entity/{symbol}")
    public HttpResponse<?> getQuoteViaJpaBySymbolEntity(@PathVariable(name = "symbol") String symbol) {

        final Optional<QuoteEntity> quoteMaybe = this.quotesRepository.findOneBySymbol(new SymbolEntity(symbol));
        if (quoteMaybe.isEmpty()) {
            CustomError customError = CustomError.builder()
                    .status(HttpStatus.NOT_FOUND.getCode())
                    .error(HttpStatus.NOT_FOUND.name())
                    .message("quote for symbol not available in db")
                    .path("/quotes/jpa/entity/" + symbol)
                    .build();
            return HttpResponse.notFound(customError);
        }
        return HttpResponse.ok(quoteMaybe.get());
    }

    @Operation(summary = "List all the quotes ordered by volumes descending. Fetched from the database via JPA.")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "quotes")
    @Get("/jpa/ordered/desc")
    public List<QuoteDto> listOrderByVolumeDesc() {
        return this.quotesRepository.listOrderByVolumeDesc();
    }

    @Operation(summary = "List all the quotes ordered by volumes ascending. Fetched from the database via JPA.")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "quotes")
    @Get("/jpa/ordered/asc")
    public List<QuoteDto> listOrderByVolumeAsc() {
        return this.quotesRepository.listOrderByVolumeAsc();
    }

    @Operation(summary = "List all the quotes with volume greater then ...")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "quotes")
    @Get(uri = "/jpa/filter")
    public List<QuoteDto> filterByVolume(@QueryValue(defaultValue = "0") BigDecimal minVolume) {
        log.debug("filterByVolume({}) invoked", minVolume);
        return this.quotesRepository.findByVolumeGreaterThanOrderByVolumeDesc(minVolume);
    }

    @Get(uri = "/jpa/pagination{?page,volume}")
    public List<QuoteDto> volumeFilterPagination(@QueryValue Optional<Integer> page, @QueryValue(defaultValue = "0") BigDecimal volume) {
        Pageable pageable = page
                .map(Pageable::from)
                .orElse(Pageable.unpaged());
        return this.quotesRepository.findByVolumeGreaterThan(volume, pageable);
    }

    @Get(uri = "/jpa/pagination/slice")
    public Slice<QuoteDto> pagination(Pageable pageable) {
        return this.quotesRepository.list(pageable);
    }

}
