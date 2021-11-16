package net.shyshkin.study.micronaut.websockets.simple;

import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.websocket.RxWebSocketClient;
import net.shyshkin.study.micronaut.websockets.client.SimpleWebSocketClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
class SimpleWebSocketServerTest {

    private static final Logger log = LoggerFactory.getLogger(SimpleWebSocketServerTest.class);

    @Inject
    @Client("http://localhost:8180")
    RxWebSocketClient client;
    private SimpleWebSocketClient webSocketClient;

    @BeforeEach
    void connect() {
        webSocketClient = client
                .connect(SimpleWebSocketClient.class, "/ws/simple/prices")
                .blockingFirst();
    }

    @Test
    void canReceiveMessagesWithClient() {

        //when
        webSocketClient.send("Hello");

        //then
        await()
                .atMost(1, TimeUnit.SECONDS)
                .untilAsserted(() -> {

                    Collection<String> messages = webSocketClient.getObservedMessages();
                    log.debug("Observed messages {} - {}", messages.size(), messages);
                    assertTrue(messages.size() >= 2);
                    List<String> observedMessages = new ArrayList<>(messages);
                    assertEquals("Connected!", observedMessages.get(0));
                    assertEquals("Not supported(Hello)", observedMessages.get(1));

                });
    }

    @Test
    void canSendReactively() {

        //when
        String result = webSocketClient.sendReactive("Reactive").blockingGet();

        //then
        log.debug("Sent {}", result);
        await()
                .atMost(1, TimeUnit.SECONDS)
                .untilAsserted(() -> {

                    Collection<String> messages = webSocketClient.getObservedMessages();
                    log.debug("Observed messages {} - {}", messages.size(), messages);
                    assertTrue(messages.size() >= 2);
                    List<String> observedMessages = new ArrayList<>(messages);
                    assertEquals("Connected!", observedMessages.get(0));
                    assertEquals("Not supported(Reactive)", observedMessages.get(1));

                });
    }
}