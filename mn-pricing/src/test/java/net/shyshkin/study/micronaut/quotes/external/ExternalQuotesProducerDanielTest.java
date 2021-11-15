package net.shyshkin.study.micronaut.quotes.external;

import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.OffsetReset;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.inject.Singleton;
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
class ExternalQuotesProducerDanielTest {

    private static final Logger log = LoggerFactory.getLogger(ExternalQuotesProducerDanielTest.class);
    private static final String PROPERTY_NAME = "ExternalQuotesProducerDanielTest";

    private static final KafkaContainer KAFKA = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"));

    private static ApplicationContext context;

    @BeforeAll
    static void beforeAll() {
        KAFKA.start();
        log.debug("Bootstrap servers: {}", KAFKA.getBootstrapServers());

        context = ApplicationContext.run(
                Map.of(
                        "kafka.bootstrap.servers", KAFKA.getBootstrapServers(),
                        PROPERTY_NAME, true
                ),
                Environment.TEST);
    }


    @AfterAll
    static void afterAll() {
        context.close();
        KAFKA.stop();
    }

    @Test
    void producing10RecordsWorks() {
        //given
        ExternalQuotesProducer externalQuotesProducer = context.getBean(ExternalQuotesProducer.class);
        ExternalQuotesObserver observer = context.getBean(ExternalQuotesObserver.class);

        //when
        IntStream.range(0, 10)
                .mapToObj(i -> randomQuote())
                .forEach(quote -> externalQuotesProducer.send(quote.getSymbol(), quote));

        //then
        await()
                .atMost(Duration.ofSeconds(1))
                .untilAsserted(() -> assertEquals(10, observer.inspected.size()));

        log.debug("---------------------------------------");
        log.debug("-----------TEST FINISHED---------------");
        log.debug("---------------------------------------");

    }

    private ExternalQuote randomQuote() {
        return ExternalQuote.builder()
                .symbol("TEST")
                .volume(randomValue())
                .lastPrice(randomValue())
                .build();
    }

    private BigDecimal randomValue() {
        return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1, 100));
    }

    @Singleton
    @Requires(env = Environment.TEST)
    @Requires(property = PROPERTY_NAME, value = "true")
    static class ExternalQuotesObserver {

        List<ExternalQuote> inspected = new ArrayList<>(10);

        @KafkaListener(offsetReset = OffsetReset.EARLIEST)
        @Topic("external-quotes")
        public void receive(@KafkaKey String symbol, ExternalQuote externalQuote) {
            log.debug("Consumed {}", externalQuote);
            if ("TEST".equals(symbol))
                inspected.add(externalQuote);
        }
    }
}