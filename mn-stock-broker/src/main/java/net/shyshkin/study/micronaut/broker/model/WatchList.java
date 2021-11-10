package net.shyshkin.study.micronaut.broker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.shyshkin.study.micronaut.broker.Symbol;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WatchList {

    private List<Symbol> symbols;

}
