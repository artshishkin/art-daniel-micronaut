package net.shyshkin.study.micronaut.broker;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "Symbol", description = "Abbreviation to uniquely identify public trades shares of a stock.")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Symbol {
    @Schema(description = "symbol value", minLength = 1, maxLength = 5)
    private String value;
}
