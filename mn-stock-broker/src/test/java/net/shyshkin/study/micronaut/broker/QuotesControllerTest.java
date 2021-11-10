package net.shyshkin.study.micronaut.broker;

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static io.micronaut.http.HttpRequest.GET;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@MicronautTest
class QuotesControllerTest {

    @Inject
    @Client("/")
    RxHttpClient client;

    @Test
    void returnQuotePerSymbol_withStatusAndType() {

        //given
        var expectedSymbol = "GOOG";

        //when
        var response = client.toBlocking().exchange("/quotes/" + expectedSymbol, Quote.class);

        //then
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
}