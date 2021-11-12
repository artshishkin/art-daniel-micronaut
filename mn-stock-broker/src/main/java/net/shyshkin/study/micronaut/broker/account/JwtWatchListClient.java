package net.shyshkin.study.micronaut.broker.account;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.reactivex.Flowable;
import net.shyshkin.study.micronaut.broker.model.WatchList;

@Client("/")
public interface JwtWatchListClient {

    String ACCOUNT_WATCHLIST_REACTIVE = "/account/watchlist-reactive";

    @Post("/login")
    BearerAccessRefreshToken login(@Body UsernamePasswordCredentials credentials);

    @Get(ACCOUNT_WATCHLIST_REACTIVE)
    Flowable<HttpResponse<WatchList>> exchangeWatchList(@Header String authorization);

    @Get(ACCOUNT_WATCHLIST_REACTIVE)
    Flowable<WatchList> retrieveWatchList(@Header String authorization);

}
