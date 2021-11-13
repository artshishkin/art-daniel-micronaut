package net.shyshkin.study.micronaut.broker;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.reactivex.Single;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.shyshkin.study.micronaut.broker.persistence.jpa.SymbolsRepository;
import net.shyshkin.study.micronaut.broker.persistence.model.SymbolEntity;
import net.shyshkin.study.micronaut.store.InMemoryStore;

import java.util.List;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/markets")
@RequiredArgsConstructor
public class MarketsController {

    private final InMemoryStore inMemoryStore;
    private final SymbolsRepository repository;

    @Operation(summary = "Return all available markets")
    @ApiResponse(
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @Tag(name = "markets") //to group together
    @Get
    public List<Symbol> all() {
        return inMemoryStore.getAllSymbols();
    }

    @Operation(summary = "Return all available markets from database using JPA")
    @ApiResponse(
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @Tag(name = "markets") //to group together
    @Get("/jpa")
    public Single<List<SymbolEntity>> allSymbolsViaJpa() {
        return Single.just(repository.findAll());
    }
}
