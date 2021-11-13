package net.shyshkin.study.micronaut.broker.persistence.model;

import io.micronaut.core.annotation.Introspected;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Introspected
public class QuoteDto {

    private Long id;
    private BigDecimal volume;

}
