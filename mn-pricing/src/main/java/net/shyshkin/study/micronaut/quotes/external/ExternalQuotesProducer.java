package net.shyshkin.study.micronaut.quotes.external;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient(id = "external-quote-producer")
public interface ExternalQuotesProducer {

    @Topic("external-quotes")
    void send(@KafkaKey String symbol, ExternalQuote externalQuote);

}
