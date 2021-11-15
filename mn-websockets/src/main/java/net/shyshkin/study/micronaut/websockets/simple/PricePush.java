package net.shyshkin.study.micronaut.websockets.simple;

import io.micronaut.scheduling.annotation.Scheduled;
import io.micronaut.websocket.WebSocketBroadcaster;
import lombok.RequiredArgsConstructor;

import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@Singleton
@RequiredArgsConstructor
public class PricePush {

    private final WebSocketBroadcaster broadcaster;

    @Scheduled(fixedDelay = "1s")
    public void push() {
        broadcaster.broadcastSync(new PriceUpdate("AMZN", randomValue()));
    }

    private BigDecimal randomValue() {
        return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1500, 2000));
    }


}
