package net.shyshkin.study.micronaut.broker;

import com.fasterxml.jackson.databind.JsonNode;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@MicronautTest
class MarketsControllerTest {

    @Inject
    @Client("/markets")
    RxHttpClient client;

    @Test
    void marketsEndpoints_shouldReturnAppropriateStatusAndMediaType() {

        //given
        var expectedStatus = HttpStatus.OK;

        //when
        var response = client.toBlocking().exchange("/", JsonNode.class);

        //then
        assertEquals(expectedStatus, response.status());
        MediaType mediaType = response.getContentType().orElseThrow();
        assertEquals(MediaType.APPLICATION_JSON, mediaType.getName());
    }

    @Test
    void marketsEndpoints_shouldReturnCorrectContentAt0Position() {

        //given
        var expectedContent = "AAPL";

        //when
        var response = client.toBlocking().exchange("/", JsonNode.class);

        //then
        JsonNode body = response.body();
        log.debug("{}", body.at("/0/value"));
        assertEquals(expectedContent, body.at("/0/value").textValue());
    }

    @Test
    void marketsEndpoints_shouldReturnCorrectJSON() {

        //given
        var expectedContent = "[{\"value\":\"AAPL\"},{\"value\":\"AMZN\"},{\"value\":\"FB\"},{\"value\":\"GOOG\"},{\"value\":\"MSFT\"},{\"value\":\"NFLX\"},{\"value\":\"TSLA\"}]";

        //when
        var response = client.toBlocking().exchange("/", JsonNode.class);

        //then
        JsonNode body = response.body();
        log.debug("{}", body);
        assertEquals(expectedContent, body.toString());
    }

    @Test
    void marketsEndpoints_shouldReturnAllSymbols() {

        //given
        var expectedSize = 7;

        //when
        var response = client.toBlocking().exchange("/", Symbol[].class);

        //then
        var body = response.body();
        log.debug("{}", body);
        assertEquals(expectedSize, body.length);
    }

}