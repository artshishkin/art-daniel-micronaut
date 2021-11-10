package net.shyshkin.study.micronaut.broker.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
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

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@MicronautTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WatchListControllerTest {

    @Inject
    @Client("/account")
    RxHttpClient client;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    AccountStore store;


    @Test
    @Order(10)
    void get_absent() throws JsonProcessingException {

        //when
        var response = client.toBlocking().exchange("/watchlist", WatchList.class);

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
        update();
        String[] symbolValues = new String[]{"NFLX", "TSLA"};

        //when
        var response = client.toBlocking().exchange("/watchlist", WatchList.class);

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
        String[] symbolValues = new String[]{"NFLX", "TSLA"};
        List<Symbol> symbols = Stream.of(symbolValues).map(Symbol::new).collect(toList());

        WatchList watchList = new WatchList(symbols);

        //when
        var response = client.toBlocking().exchange(HttpRequest.PUT("/watchlist", watchList), WatchList.class);

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
        update();
        UUID accountId = WatchListController.ACCOUNT_ID;
        assertThat(store.getWatchList(accountId).getSymbols()).isNotNull();

        //when
        var response = client.toBlocking().exchange(HttpRequest.DELETE("/watchlist/" + accountId), WatchList.class);

        //then
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", HttpStatus.NO_CONTENT);
        assertThat(response.body()).isNull();
        assertThat(store.getWatchList(accountId).getSymbols()).isNull();
    }
}