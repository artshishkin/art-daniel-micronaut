package net.shyshkin.study.micronaut.websockets.client;

import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.ClientWebSocket;
import io.micronaut.websocket.annotation.OnClose;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

@ClientWebSocket("/ws/simple/prices")
public abstract class SimpleWebSocketClient implements AutoCloseable {

    private WebSocketSession session;
    private final Collection<String> observedMessages = new ConcurrentLinkedQueue<>();

    @OnOpen
    public void onOpen(WebSocketSession session) {
        this.session = session;
    }

    @OnMessage
    public void onMessage(String message) {
        this.observedMessages.add(message);
    }

    @OnClose
    @Override
    public void close() throws Exception {
        this.session.close();
    }

    public WebSocketSession getSession() {
        return session;
    }

    public Collection<String> getObservedMessages() {
        return observedMessages;
    }

    public abstract void send(String message);
}
