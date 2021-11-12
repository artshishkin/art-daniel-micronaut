package net.shyshkin.study.micronaut.broker.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.micronaut.broker.Symbol;
import net.shyshkin.study.micronaut.broker.model.WatchList;
import net.shyshkin.study.micronaut.store.AccountStore;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static io.micronaut.http.HttpRequest.*;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@MicronautTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WatchListControllerReactiveTest {

    private static final UUID TEST_ACCOUNT_ID = WatchListControllerReactive.ACCOUNT_ID;
    public static final String ACCOUNT_WATCHLIST_REACTIVE = "/account/watchlist-reactive";
    private static String globalAccessToken = null;

    @Inject
    @Client("/")
    RxHttpClient client;

    @Inject
    @Client("/")
    JwtWatchListClient jwtClient;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    AccountStore store;

    @Test
    @Order(10)
    void get_absent_exchange() throws JsonProcessingException {

        //given
        var accessToken = getAccessToken();

        //when
        var exchange = jwtClient.exchangeWatchList("Bearer " + accessToken);

        //then
        Single<HttpResponse<WatchList>> result = exchange.singleOrError();
        HttpResponse<WatchList> response = result.blockingGet();
        log.debug("{}", objectMapper.writeValueAsString(response));
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", HttpStatus.OK)
                .satisfies(resp -> assertThat(resp.getContentType()).hasValue(MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.body())
                .hasAllNullFieldsOrProperties();
    }

    @Test
    @Order(10)
    void get_absent_retrieve() throws JsonProcessingException {

        //given
        var accessToken = getAccessToken();

        //when
        Single<WatchList> result = jwtClient
                .retrieveWatchList("Bearer " + accessToken)
                .singleOrError();

        //then
        WatchList watchList = result.blockingGet();
        log.debug("{}", objectMapper.writeValueAsString(watchList));
        assertThat(watchList).hasAllNullFieldsOrProperties();
    }

    @Test
    @Order(30)
    void get_present_exchange() throws JsonProcessingException {

        //given
        var accessToken = getAccessToken();

        update();
        String[] symbolValues = new String[]{"NFLX", "TSLA"};
        TestObserver<HttpResponse<WatchList>> observer = new TestObserver<>();

        //when
        jwtClient.exchangeWatchList("Bearer " + accessToken)

                .singleOrError()
                .doOnEvent((resp, err) -> log.debug("{}", objectMapper.writeValueAsString(resp)))
                .subscribe(observer);

        //then
        observer.awaitTerminalEvent(1, TimeUnit.SECONDS);
        observer
                .assertComplete()
                .assertNoErrors()
                .assertValueCount(1);

        HttpResponse<WatchList> response = observer.values().get(0);
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", HttpStatus.OK)
                .satisfies(resp -> assertThat(resp.getContentType()).hasValue(MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.body().getSymbols())
                .extracting(Symbol::getValue)
                .containsExactlyInAnyOrder(symbolValues);

    }

    @Test
    @Order(31)
    void get_present_retrieve() throws JsonProcessingException {

        //given
        var accessToken = getAccessToken();

        update();
        String[] symbolValues = new String[]{"NFLX", "TSLA"};
        TestObserver<WatchList> observer = new TestObserver<>();

        //when
        jwtClient.retrieveWatchList("Bearer " + accessToken)

                .singleOrError()
                .doOnEvent((watchList, err) -> log.debug("{}", objectMapper.writeValueAsString(watchList)))
                .subscribe(observer);

        //then
        observer.awaitTerminalEvent(1, TimeUnit.SECONDS);
        observer
                .assertComplete()
                .assertNoErrors()
                .assertValueCount(1);

        WatchList watchList = observer.values().get(0);
        assertThat(watchList.getSymbols())
                .extracting(Symbol::getValue)
                .containsExactlyInAnyOrder(symbolValues);

    }

    @Order(32)
    @ParameterizedTest
    @ValueSource(strings = {"/single", "/flowable"})
    void getAsSingleOrFlowable(String uri) throws JsonProcessingException {

        //given
        var accessToken = getAccessToken();

        update();
        String[] symbolValues = new String[]{"NFLX", "TSLA"};
        TestObserver<HttpResponse<WatchList>> observer = new TestObserver<>();

        //when
        var request = GET(ACCOUNT_WATCHLIST_REACTIVE + uri).bearerAuth(accessToken);
        client.exchange(request, WatchList.class)

                .singleOrError()
                .doOnEvent((resp, err) -> log.debug("{}", objectMapper.writeValueAsString(resp)))
                .subscribe(observer);

        //then
        observer.awaitTerminalEvent(1, TimeUnit.SECONDS);
        observer
                .assertComplete()
                .assertNoErrors()
                .assertValueCount(1);

        HttpResponse<WatchList> response = observer.values().get(0);
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", HttpStatus.OK)
                .satisfies(resp -> assertThat(resp.getContentType()).hasValue(MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.body().getSymbols())
                .extracting(Symbol::getValue)
                .containsExactlyInAnyOrder(symbolValues);

    }

    @Test
    @Order(20)
    void update() throws JsonProcessingException {
        //given
        var accessToken = getAccessToken();

        String[] symbolValues = new String[]{"NFLX", "TSLA"};
        List<Symbol> symbols = Stream.of(symbolValues).map(Symbol::new).collect(toList());

        WatchList watchList = new WatchList(symbols);

        //when

        var put = PUT(ACCOUNT_WATCHLIST_REACTIVE, watchList).bearerAuth(accessToken);
        var response = client
                .exchange(put, WatchList.class)
                .singleOrError()
                .blockingGet();

        //then
        log.debug("{}", objectMapper.writeValueAsString(response));
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", HttpStatus.OK)
                .satisfies(resp -> assertThat(resp.getContentType()).hasValue(MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.body().getSymbols())
                .extracting(Symbol::getValue)
                .containsExactlyInAnyOrder(symbolValues);
    }

    @Test
    @Order(40)
    void delete() throws JsonProcessingException {

        //given
        var accessToken = getAccessToken();

        update();
        UUID accountId = TEST_ACCOUNT_ID;
        assertThat(store.getWatchList(accountId).getSymbols()).isNotNull();

        //when
        var request = DELETE(ACCOUNT_WATCHLIST_REACTIVE + "/" + accountId).bearerAuth(accessToken);
        var response = client.toBlocking().exchange(request, WatchList.class);

        //then
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", HttpStatus.NO_CONTENT);
        assertThat(response.body()).isNull();
        assertThat(store.getWatchList(accountId).getSymbols()).isNull();
    }

    private String getAccessToken() {

        if (globalAccessToken == null) {
            var credentials = new UsernamePasswordCredentials("my-user", "secret");
            var token = this.jwtClient.login(credentials);
            assertThat(token).isNotNull();
            assertThat(token.getUsername()).isEqualTo("my-user");
            globalAccessToken = token.getAccessToken();
        }
        return globalAccessToken;
    }
}