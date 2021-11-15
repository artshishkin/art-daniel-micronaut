package net.shyshkin.study.micronaut.websockets.simple;

import io.micronaut.websocket.CloseReason;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.OnClose;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import io.micronaut.websocket.annotation.ServerWebSocket;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;

@Slf4j
@ServerWebSocket("/ws/simple/prices")
public class SimpleWebSocketServer {

    @OnOpen
    public Publisher<String> onOpen(WebSocketSession session) {
        return session.send("Connected!");
    }

    @OnMessage
    public Publisher<String> onMessage(String message, WebSocketSession session) {
        log.debug("Received message: {}. Session Id: {}", message, session.getId());
        if ("disconnect me".equalsIgnoreCase(message)) {
            log.debug("Client close requested!");
            session.close(CloseReason.NORMAL);
            return Flowable.empty();
        }
        return session.send("Not supported(" + message + ")");
    }

    @OnClose
    public void onClose(WebSocketSession session) {
        log.debug("Session closed {}", session.getId());
    }


}
