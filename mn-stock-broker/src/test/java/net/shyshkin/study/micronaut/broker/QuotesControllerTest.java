package net.shyshkin.study.micronaut.broker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.micronaut.store.InMemoryStore;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

import static io.micronaut.http.HttpRequest.GET;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@MicronautTest
class QuotesControllerTest {

    @Inject
    @Client("/")
    RxHttpClient client;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    InMemoryStore store;

    @Test
    void returnQuotePerSymbol_withStatusAndType() throws JsonProcessingException {

        //given
        var expectedSymbol = "GOOG";

        //when
        var response = client.toBlocking().exchange("/quotes/" + expectedSymbol, Quote.class);

        //then
        log.debug("{}", objectMapper.writeValueAsString(response));
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", HttpStatus.OK)
                .satisfies(resp -> assertThat(resp.getContentType()).hasValue(MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.body())
                .hasNoNullFieldsOrProperties()
                .extracting(Quote::getSymbol)
                .extracting("value")
                .isEqualTo(expectedSymbol);
    }

    @Test
    void returnQuotePerSymbol() {

        //given
        var expectedSymbol = "APPL";

        //when
        var result = client.toBlocking().retrieve(GET("/quotes/" + expectedSymbol), Argument.of(Quote.class));

        //then
        assertThat(result)
                .hasNoNullFieldsOrProperties()
                .extracting(Quote::getSymbol)
                .extracting("value")
                .isEqualTo(expectedSymbol);
    }

    @Test
    void returnQuotePerSymbol_absent() throws JsonProcessingException {

        //given
        var expectedSymbol = "NO_QUOTE";

        //when
        var response = client.toBlocking().exchange("/quotes/" + expectedSymbol);

        //then
        log.debug("{}", objectMapper.writeValueAsString(response));
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", HttpStatus.NO_CONTENT);
        assertThat(response.body()).isNull();
    }

    @Test
    void returnQuotePerSymbol_afterUpdate() {

        //given
        var expectedSymbol = "APPL";
        Quote quote = initRandomQuote(expectedSymbol);
        this.store.updateQuote(quote);

        //when
        var result = client.toBlocking().retrieve(GET("/quotes/" + expectedSymbol), Argument.of(Quote.class));

        //then
        assertThat(result)
                .isEqualToComparingFieldByField(quote);
    }

    private Quote initRandomQuote(String symbol) {
        return Quote.builder()
                .symbol(new Symbol(symbol))
                .bid(randomValue())
                .ask(randomValue())
                .lastPrice(randomValue())
                .volume(randomValue())
                .build();
    }

    private BigDecimal randomValue() {
        return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1, 100));
    }


}