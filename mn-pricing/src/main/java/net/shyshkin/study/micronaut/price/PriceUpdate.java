package net.shyshkin.study.micronaut.price;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Introspected
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceUpdate {

    private String symbol;
    private BigDecimal price;

}
