package net.shyshkin.study.micronaut.health;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class HealthTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    public void testHealthRespondsOK() {
        Map m = client.toBlocking().retrieve(HttpRequest.GET("/health"), Map.class);

        assertNotNull(m);
        assertTrue(m.containsKey("status"));
        assertEquals(m.get("status"), "UP");
    }
}
