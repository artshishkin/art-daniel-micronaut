package net.shyshkin.study.micronaut.broker.account;


import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.micronaut.broker.model.WatchList;
import net.shyshkin.study.micronaut.store.AccountStore;

import java.util.UUID;

import static io.micronaut.http.MediaType.APPLICATION_JSON;

@Slf4j
@Controller("/account/watchlist-reactive")
@RequiredArgsConstructor
public class WatchListControllerReactive {

    static final UUID ACCOUNT_ID = UUID.randomUUID();

    private final AccountStore store;

    @Get(produces = APPLICATION_JSON)
    @ExecuteOn(TaskExecutors.IO)
    public WatchList get() {
        log.debug("GET /account/watchlist-reactive");
        return store.getWatchList(ACCOUNT_ID);
    }

    @Put(
            consumes = APPLICATION_JSON,
            produces = APPLICATION_JSON
    )
    @ExecuteOn(TaskExecutors.IO)
    public WatchList update(@Body WatchList watchList) {
        log.debug("PUT /account/watchlist-reactive");
        return store.updateWatchList(ACCOUNT_ID, watchList);
    }

    @Delete("/{accountId}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<?> delete(@PathVariable UUID accountId) {
        log.debug("DELETE /account/watchlist-reactive");
        store.deleteWatchList(accountId);
        return HttpResponse.noContent();
    }

}
