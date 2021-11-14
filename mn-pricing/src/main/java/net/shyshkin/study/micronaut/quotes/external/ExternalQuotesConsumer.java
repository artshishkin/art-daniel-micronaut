package net.shyshkin.study.micronaut.quotes.external;

import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@KafkaListener(
        clientId = "mn-pricing-external-quote-consumer",
        groupId = "external-quote-consumer"
)
public class ExternalQuotesConsumer {

    @Topic("external-quotes")
    public void receive(@KafkaKey String symbol,
                        ExternalQuote externalQuote,
                        long offset,
                        int partition,
                        String topic,
                        long timestamp) {
        log.debug("Got {} quote: {}. Offset: {}", symbol, externalQuote, offset);
    }

}
