package net.shyshkin.study.micronaut.broker.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.micronaut.broker.Symbol;
import net.shyshkin.study.micronaut.broker.model.WatchList;
import net.shyshkin.study.micronaut.store.AccountStore;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static io.micronaut.http.HttpRequest.*;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@MicronautTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WatchListControllerTest {

    public static final String ACCOUNT_WATCHLIST = "/account/watchlist";
    @Inject
    @Client("/")
    RxHttpClient client;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    AccountStore store;

    private static String globalAccessToken = null;

    @Test
    @Order(10)
    void get_absent() throws JsonProcessingException {

        //given
        var accessToken = getAccessToken();

        //when
        MutableHttpRequest<Object> request = GET(ACCOUNT_WATCHLIST).bearerAuth(accessToken);
        var response = client.toBlocking().exchange(request, WatchList.class);

        //then
        log.debug("{}", objectMapper.writeValueAsString(response));
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", HttpStatus.OK)
                .satisfies(resp -> assertThat(resp.getContentType()).hasValue(MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.body())
                .hasAllNullFieldsOrProperties();
    }

    private String getAccessToken() {

        if (globalAccessToken == null) {
            var credentials = new UsernamePasswordCredentials("my-user", "secret");
            MutableHttpRequest<Object> login = POST("/login", credentials);
//        var httpResponse = client.toBlocking().exchange(login, ObjectNode.class);
            var httpResponse = client.toBlocking().exchange(login, BearerAccessRefreshToken.class);
            try {
                log.debug("{}", objectMapper.writeValueAsString(httpResponse));
                log.debug("{}", objectMapper.writeValueAsString(httpResponse.body()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            var token = httpResponse.body();
            assertThat(token).isNotNull();
            assertThat(token.getUsername()).isEqualTo("my-user");
            globalAccessToken = token.getAccessToken();
        }
        return globalAccessToken;
    }


    @Test
    @Order(12)
    void get_absent_basicAuth() throws JsonProcessingException {

        //given
        MutableHttpRequest<Object> request = GET(ACCOUNT_WATCHLIST).basicAuth("my-user", "secret");

        //when
        var response = client.toBlocking().exchange(request, WatchList.class);

        //then
        log.debug("{}", objectMapper.writeValueAsString(response));
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", HttpStatus.OK)
                .satisfies(resp -> assertThat(resp.getContentType()).hasValue(MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.body())
                .hasAllNullFieldsOrProperties();
    }

    @Test
    @Order(30)
    void get_present() throws JsonProcessingException {

        //given
        var accessToken = getAccessToken();

        update();
        String[] symbolValues = new String[]{"NFLX", "TSLA"};

        //when
        var request = GET(ACCOUNT_WATCHLIST).bearerAuth(accessToken);
        var response = client.toBlocking().exchange(request, WatchList.class);

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
    @Order(20)
    void update() throws JsonProcessingException {
        //given
        var accessToken = getAccessToken();

        String[] symbolValues = new String[]{"NFLX", "TSLA"};
        List<Symbol> symbols = Stream.of(symbolValues).map(Symbol::new).collect(toList());

        WatchList watchList = new WatchList(symbols);

        //when
        MutableHttpRequest<WatchList> request = PUT(ACCOUNT_WATCHLIST, watchList).bearerAuth(accessToken);
        var response = client.toBlocking().exchange(request, WatchList.class);

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
        UUID accountId = WatchListController.ACCOUNT_ID;
        assertThat(store.getWatchList(accountId).getSymbols()).isNotNull();

        //when
        MutableHttpRequest<Object> request = DELETE(ACCOUNT_WATCHLIST + "/" + accountId).bearerAuth(accessToken);
        var response = client.toBlocking().exchange(request, WatchList.class);

        //then
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", HttpStatus.NO_CONTENT);
        assertThat(response.body()).isNull();
        assertThat(store.getWatchList(accountId).getSymbols()).isNull();
    }
}