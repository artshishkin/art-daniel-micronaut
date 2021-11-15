package net.shyshkin.study.micronaut.websockets.simple;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PriceUpdate {

    private final String symbol;
    private final BigDecimal price;

}
