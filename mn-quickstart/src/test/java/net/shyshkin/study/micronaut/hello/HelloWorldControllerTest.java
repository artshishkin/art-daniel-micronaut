package net.shyshkin.study.micronaut.hello;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@MicronautTest
class HelloWorldControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void helloWorldEndpointRespondsWithProperContent() {

        //given
        String expectedResponse = "Hello from Service";

        //when
        String response = client.toBlocking().retrieve("/hello");

        //then
        assertEquals(expectedResponse, response);
    }

    @Test
    void helloWorldEndpointRespondsWithProperStatusAndMediaType() {

        //given
        var expectedStatus = HttpStatus.OK;

        //when
        var response = client.toBlocking().exchange("/hello", String.class);

        //then
        assertEquals(expectedStatus, response.status());
        MediaType mediaType = response.getContentType().orElseThrow();
        assertEquals(MediaType.TEXT_PLAIN, mediaType.getName());
    }

    @Test
    void configEndpoint_shouldRespondWithProperStatusAndContent() {

        //given
        var expectedContent = "Hello from application.yml";
        var expectedStatus = HttpStatus.OK;

        //when
        var response = client.toBlocking().exchange("/hello/config", String.class);

        //then
        assertEquals(expectedContent, response.body());
        assertEquals(expectedStatus, response.status());
        MediaType mediaType = response.getContentType().orElseThrow();
        assertEquals(MediaType.TEXT_PLAIN, mediaType.getName());
    }

    @Test
    void translationEndpoint_shouldRespondWithProperStatusAndContent() {

        //given
        var expectedContent = "{\"de\":\"Hallo Welt\",\"en\":\"Hello World\"}";
        var expectedStatus = HttpStatus.OK;

        //when
        var response = client.toBlocking().exchange("/hello/translation", JsonNode.class);

        //then
        JsonNode body = response.body();
        assertEquals("Hello World", body.at("/en").textValue());
        assertEquals("Hallo Welt", body.at("/de").textValue());
        assertEquals(expectedStatus, response.status());
        MediaType mediaType = response.getContentType().orElseThrow();
        assertEquals(MediaType.APPLICATION_JSON, mediaType.getName());
    }

    @Test
    void returnGreetingAsJson_ObjectNode() {

        //when
        var result = client.toBlocking().retrieve("/hello/json", ObjectNode.class);

        //then
        log.debug("{}", result);
    }

    @Test
    void returnGreetingAsJson_String() {

        //when
        var result = client.toBlocking().retrieve("/hello/json", String.class);

        //then
        log.debug("{}", result);
    }

}