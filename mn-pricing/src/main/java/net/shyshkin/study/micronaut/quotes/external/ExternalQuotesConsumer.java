package net.shyshkin.study.micronaut.quotes.external;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.OffsetReset;
import io.micronaut.configuration.kafka.annotation.Topic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.micronaut.price.PriceUpdate;
import net.shyshkin.study.micronaut.price.PriceUpdateProducer;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@KafkaListener(
        clientId = "mn-pricing-external-quote-consumer",
        groupId = "external-quote-consumer",
        batch = true,
        offsetReset = OffsetReset.EARLIEST
)
@RequiredArgsConstructor
public class ExternalQuotesConsumer {

    private final PriceUpdateProducer priceUpdateProducer;

    @Topic("external-quotes")
    public void receive(List<ExternalQuote> externalQuotes) {
        log.debug("Consuming batch of external quotes {}", externalQuotes);
        //Forward price update
        var priceUpdates = externalQuotes.stream()
                .map(this::toPriceUpdate)
                .collect(Collectors.toList());
        priceUpdateProducer
                .send(priceUpdates)
                .subscribe(
                        meta -> log.debug("Record sent tot topic {} at offset {}", meta.topic(), meta.offset()),
                        e -> log.error("Failed to produce:", e)
                );
    }

    private PriceUpdate toPriceUpdate(ExternalQuote externalQuote) {
        return PriceUpdate.builder()
                .symbol(externalQuote.getSymbol())
                .price(externalQuote.getLastPrice())
                .build();
    }

}
