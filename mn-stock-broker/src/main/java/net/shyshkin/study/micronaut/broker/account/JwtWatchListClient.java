package net.shyshkin.study.micronaut.broker.account;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.reactivex.Flowable;
import io.reactivex.Single;
import net.shyshkin.study.micronaut.broker.model.WatchList;

import java.util.UUID;

@Client("/")
public interface JwtWatchListClient {

    String ACCOUNT_WATCHLIST_REACTIVE = "/account/watchlist-reactive";

    @Post("/login")
    BearerAccessRefreshToken login(@Body UsernamePasswordCredentials credentials);

    @Get(ACCOUNT_WATCHLIST_REACTIVE)
    Flowable<HttpResponse<WatchList>> exchangeWatchList(@Header String authorization);

    @Get(ACCOUNT_WATCHLIST_REACTIVE)
    Flowable<WatchList> retrieveWatchList(@Header String authorization);

    @Get(ACCOUNT_WATCHLIST_REACTIVE + "/single")
    Single<HttpResponse<WatchList>> exchangeWatchListAsSingle(@Header String authorization);

    @Get(ACCOUNT_WATCHLIST_REACTIVE + "/single")
    Single<WatchList> retrieveWatchListAsSingle(@Header String authorization);

    @Get(ACCOUNT_WATCHLIST_REACTIVE + "/flowable")
    Flowable<HttpResponse<WatchList>> exchangeWatchListAsFlowable(@Header String authorization);

    @Get(ACCOUNT_WATCHLIST_REACTIVE + "/flowable")
    Flowable<WatchList> retrieveWatchListAsFlowable(@Header String authorization);

    @Put(ACCOUNT_WATCHLIST_REACTIVE)
    Single<HttpResponse<WatchList>> updateWatchList(@Header String authorization, @Body WatchList watchList);

    @Delete(ACCOUNT_WATCHLIST_REACTIVE + "/{accountId}")
    HttpResponse<?> deleteWatchList(@Header String authorization, @PathVariable UUID accountId);

}
