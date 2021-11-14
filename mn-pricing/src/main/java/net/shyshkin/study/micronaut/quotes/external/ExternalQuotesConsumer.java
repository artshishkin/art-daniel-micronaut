package net.shyshkin.study.micronaut.quotes.external;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@KafkaListener(
        clientId = "mn-pricing-external-quote-consumer",
        groupId = "external-quote-consumer",
        batch = true
)
public class ExternalQuotesConsumer {

    @Topic("external-quotes")
    public void receive(List<ExternalQuote> externalQuotes) {
        log.debug("Consuming batch of external quotes {}", externalQuotes);
    }

}
