package net.shyshkin.study.micronaut.quotes.external;

import io.micronaut.configuration.kafka.annotation.*;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import net.shyshkin.study.micronaut.price.PriceUpdate;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
class ExternalQuotesConsumerTest {

    private static final Logger log = LoggerFactory.getLogger(ExternalQuotesConsumerTest.class);
    private static final String PROPERTY_NAME = "ExternalQuotesConsumerTest";

    // will be shared between test methods
    @Container
    private static final KafkaContainer KAFKA = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"));

    private static ApplicationContext context;

    @BeforeAll
    static void beforeAll() {
        log.debug("Bootstrap servers: {}", KAFKA.getBootstrapServers());

        context = ApplicationContext.run(
                Map.of(
                        "kafka.bootstrap.servers", KAFKA.getBootstrapServers(),
                        "kafka.consumers.external-quote-consumer.auto.offset.reset", "earliest",
                        PROPERTY_NAME, true
                ),
                Environment.TEST);
    }


    @AfterAll
    static void afterAll() {
        context.close();
    }

    @Test
    void consumingPriceUpdateWorksCorrectly() throws InterruptedException {
        //given
        var quotesProducer = context.getBean(TestQuotesProducer.class);
        var observer = context.getBean(PriceUpdateObserver.class);

        //when
        IntStream.range(0, 10)
                .mapToObj(i -> randomQuote())
                .forEach(quote -> quotesProducer.send(quote.getSymbol(), quote));

        //then
        log.debug("Start waiting for 10 incoming messages");
        await()
                .atMost(Duration.ofSeconds(3))
                .untilAsserted(() -> assertEquals(10, observer.inspected.size()));

        log.debug("---------------------------------------");
        log.debug("-----------TEST FINISHED---------------");
        log.debug("---------------------------------------");

    }

    private ExternalQuote randomQuote() {
        return ExternalQuote.builder()
                .symbol("TEST-" + ThreadLocalRandom.current().nextInt(100))
                .volume(randomValue())
                .lastPrice(randomValue())
                .build();
    }

    private BigDecimal randomValue() {
        return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1, 100));
    }

    @KafkaListener(
            offsetReset = OffsetReset.EARLIEST,
            clientId = "price-update-observer"
    )
    @Requires(env = Environment.TEST)
    @Requires(property = PROPERTY_NAME, value = "true")
    static class PriceUpdateObserver {

        List<PriceUpdate> inspected = new ArrayList<>();

        @Topic("price_update")
        public void receive(List<PriceUpdate> priceUpdates) {
            log.debug("Consumed {}", priceUpdates);
            inspected.addAll(priceUpdates);
        }
    }

    @KafkaClient(id = "test-quote-producer")
    @Requires(env = Environment.TEST)
    @Requires(property = PROPERTY_NAME, value = "true")
    public interface TestQuotesProducer {

        @Topic("external-quotes")
        void send(@KafkaKey String symbol, ExternalQuote externalQuote);

    }

}