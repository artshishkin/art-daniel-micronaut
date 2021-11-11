package net.shyshkin.study.micronaut.broker.account;


import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.micronaut.broker.model.WatchList;
import net.shyshkin.study.micronaut.store.AccountStore;

import javax.inject.Named;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

import static io.micronaut.http.MediaType.APPLICATION_JSON;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Slf4j
@Controller("/account/watchlist-reactive")
public class WatchListControllerReactive {

    static final UUID ACCOUNT_ID = UUID.randomUUID();

    private final Scheduler scheduler;

    private final AccountStore store;

    public WatchListControllerReactive(
            @Named(TaskExecutors.IO) ExecutorService executorService,
            AccountStore store) {
        this.store = store;
        this.scheduler = Schedulers.from(executorService);
    }

    @Get(produces = APPLICATION_JSON)
    @ExecuteOn(TaskExecutors.IO)
    public WatchList get() {
        log.debug("GET /account/watchlist-reactive");
        return store.getWatchList(ACCOUNT_ID);
    }

    @Get(uri = "/single", produces = APPLICATION_JSON)
    public Single<WatchList> getAsSingle() {
        return Single
                .fromCallable(() -> {
                    log.debug("GET /account/watchlist-reactive/single");
                    return store.getWatchList(ACCOUNT_ID);
                })
                .subscribeOn(scheduler);
    }

    @Get(uri = "/flowable", produces = APPLICATION_JSON)
    public Flowable<WatchList> getAsFlowable() {
        return Single
                .fromCallable(() -> {
                    log.debug("GET /account/watchlist-reactive/flowable");
                    return store.getWatchList(ACCOUNT_ID);
                })
                .toFlowable()
                .subscribeOn(scheduler);
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
