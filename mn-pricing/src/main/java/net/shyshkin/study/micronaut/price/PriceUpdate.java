package net.shyshkin.study.micronaut.price;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceUpdate {

    private String symbol;
    private BigDecimal price;

}
