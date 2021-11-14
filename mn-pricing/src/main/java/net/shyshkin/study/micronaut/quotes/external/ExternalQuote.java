package net.shyshkin.study.micronaut.quotes.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExternalQuote {

    private String symbol;
    private BigDecimal lastPrice;
    private BigDecimal volume;

}
