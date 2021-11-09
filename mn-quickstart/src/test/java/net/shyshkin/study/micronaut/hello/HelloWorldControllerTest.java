package net.shyshkin.study.micronaut.hello;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void helloWorldEndpointRespondsWithProperStatusAnd() {

        //given
        var expectedStatus = HttpStatus.OK;

        //when
        var response = client.toBlocking().exchange("/hello", String.class);

        //then
        assertEquals(expectedStatus, response.status());
        MediaType mediaType = response.getContentType().orElseThrow();
        assertEquals(MediaType.TEXT_PLAIN, mediaType.getName());
    }
}