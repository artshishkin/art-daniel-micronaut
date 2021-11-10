package net.shyshkin.study.micronaut.broker.account;


import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.micronaut.broker.model.WatchList;
import net.shyshkin.study.micronaut.store.AccountStore;

import java.util.UUID;

import static io.micronaut.http.MediaType.APPLICATION_JSON;

@Slf4j
@Controller("/account/watchlist")
@RequiredArgsConstructor
public class WatchListController {

    static final UUID ACCOUNT_ID = UUID.randomUUID();

    private final AccountStore store;

    @Get(produces = APPLICATION_JSON)
    public WatchList get() {
        log.debug("GET /account/watchlist");
        return store.getWatchList(ACCOUNT_ID);
    }

    @Put(
            consumes = APPLICATION_JSON,
            produces = APPLICATION_JSON
    )
    public WatchList update(@Body WatchList watchList) {
        log.debug("PUT /account/watchlist");
        return store.updateWatchList(ACCOUNT_ID, watchList);
    }

    @Delete("/{accountId}")
    public HttpResponse<?> delete(@PathVariable UUID accountId) {
        log.debug("DELETE /account/watchlist");
        store.deleteWatchList(accountId);
        return HttpResponse.noContent();
    }

}
